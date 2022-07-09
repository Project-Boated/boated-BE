package com.projectboated.backend.domain.account.account.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.entity.KakaoAccount;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.profileimage.service.AwsS3ProfileImageService;
import com.projectboated.backend.domain.account.account.service.condition.AccountUpdateCond;
import com.projectboated.backend.domain.account.account.service.exception.AccountNicknameAlreadyExistsException;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.account.account.service.exception.AccountPasswordWrong;
import com.projectboated.backend.domain.account.account.service.exception.AccountUsernameAlreadyExistsException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.common.exception.business.CommonIOException;
import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.account.profileimage.entity.UploadFileProfileImage;
import com.projectboated.backend.domain.account.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.domain.uploadfile.repository.UploadFileRepository;
import com.projectboated.backend.security.service.KakaoWebService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoWebService kakaoWebService;
    private final AwsS3ProfileImageService awsS3ProfileService;
    private final AccountNicknameService nicknameService;
    private final UploadFileRepository uploadFileRepository;
    private final ProfileImageRepository profileImageRepository;

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Transactional
    public Account save(Account account) {

        if (isExistsUsername(account.getUsername())) {
            throw new AccountUsernameAlreadyExistsException(ErrorCode.ACCOUNT_USERNAME_EXISTS);
        }

        if (nicknameService.isNotSameAndExistsNickname(account.getNickname())) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }

        account.changePassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    private boolean isExistsUsername(String username) {
        return Objects.nonNull(username) && accountRepository.existsByUsername(username);
    }

    @Transactional
    public void updateProfile(Account account, AccountUpdateCond updateCond) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!(account instanceof KakaoAccount)) {
            if (!checkValidPassword(findAccount, updateCond.getOriginalPassword())) {
                throw new AccountPasswordWrong(ErrorCode.ACCOUNT_PASSWORD_WRONG);
            }
        }

        if (!equalsOrNull(account.getNickname(), updateCond.getNickname()) &&
                accountRepository.existsByNickname(updateCond.getNickname())) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }

        if (updateCond.getNewPassword() != null) {
            updateCond.setNewPassword(passwordEncoder.encode(updateCond.getNewPassword()));
        }

        if (updateCond.getProfileImageFile() != null) {
            MultipartFile file = updateCond.getProfileImageFile();
            UploadFile uploadFile = new UploadFile(file.getOriginalFilename(), UUID.randomUUID().toString(), file.getContentType());
            UploadFileProfileImage uploadFileProfileImage = new UploadFileProfileImage(uploadFile);
            try {
                awsS3ProfileService.uploadProfileImage(account, uploadFileProfileImage, updateCond.getProfileImageFile());
            } catch (IOException e) {
                throw new CommonIOException(ErrorCode.COMMON_IO_EXCEPTION, e);
            }

            ProfileImage oldProfileImage = (ProfileImage) Hibernate.unproxy(findAccount.getProfileImage());
            if (oldProfileImage instanceof UploadFileProfileImage oldUploadFileProfileImage) {
                UploadFile oldUploadFile = oldUploadFileProfileImage.getUploadFile();
                uploadFileRepository.delete(oldUploadFile);
            }
            profileImageRepository.delete(oldProfileImage);

            uploadFileRepository.save(uploadFile);
            profileImageRepository.save(uploadFileProfileImage);

            findAccount.changeProfileImage(uploadFileProfileImage);
        }

        findAccount.changeNickname(updateCond.getNickname());
        findAccount.changePassword(updateCond.getNewPassword());
    }

    private boolean checkValidPassword(Account account, String password) {
        if (password == null) {
            return false;
        }
        return passwordEncoder.matches(password, account.getPassword());
    }

    @Transactional
    public void delete(Account account) {
        if (account instanceof KakaoAccount kakaoAccount) {
            kakaoWebService.disconnect(kakaoAccount.getKakaoId());
        }
        accountRepository.delete(account);
    }

    public boolean equalsOrNull(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return true;
        }
        return s1.equals(s2);
    }
}
