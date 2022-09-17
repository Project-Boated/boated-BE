package com.projectboated.backend.account.account.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.entity.KakaoAccount;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.condition.AccountUpdateCond;
import com.projectboated.backend.account.account.service.exception.AccountNicknameAlreadyExistsException;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.account.account.service.exception.AccountPasswordWrong;
import com.projectboated.backend.account.account.service.exception.AccountUsernameAlreadyExistsException;
import com.projectboated.backend.account.profileimage.entity.UploadFileProfileImage;
import com.projectboated.backend.account.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.infra.aws.AwsS3ProfileImageService;
import com.projectboated.backend.security.service.KakaoWebService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final KakaoWebService kakaoWebService;
    private final AwsS3ProfileImageService awsS3ProfileService;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final ProfileImageRepository profileImageRepository;

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Transactional
    public Account save(Account account) {
        assert (account.getUsername() != null);
        assert (account.getNickname() != null);
        assert (account.getPassword() != null);

        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new AccountUsernameAlreadyExistsException(ErrorCode.ACCOUNT_USERNAME_EXISTS);
        }

        if (accountRepository.existsByNickname(account.getNickname())) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }

        account.changePassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Transactional
    public void updateProfile(Long accountId, AccountUpdateCond updateCond) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!(account instanceof KakaoAccount)) {
            if (!checkValidPassword(account, updateCond.getOriginalPassword())) {
                throw new AccountPasswordWrong(ErrorCode.ACCOUNT_PASSWORD_WRONG);
            }
        }

        if (updateCond.getNickname() != null &&
                !updateCond.getNickname().equals(account.getNickname()) &&
                accountRepository.existsByNickname(updateCond.getNickname())) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }

        if (updateCond.getNewPassword() != null) {
            account.changePassword(passwordEncoder.encode(updateCond.getNewPassword()));
        }

        if (updateCond.getProfileImageFile() != null) {
            MultipartFile file = updateCond.getProfileImageFile();
            UploadFile uploadFile = UploadFile.builder()
                    .originalFileName(file.getOriginalFilename())
                    .saveFileName(UUID.randomUUID().toString())
                    .mediaType(file.getContentType())
                    .build();
            UploadFileProfileImage uploadFileProfileImage = new UploadFileProfileImage(uploadFile);

            awsS3ProfileService.uploadProfileImage(account, uploadFileProfileImage, updateCond.getProfileImageFile());
            profileImageRepository.save(uploadFileProfileImage);
            account.changeProfileImage(uploadFileProfileImage);
        }

        account.changeNickname(updateCond.getNickname());
    }

    private boolean checkValidPassword(Account account, String password) {
        if (password == null) {
            return false;
        }
        return passwordEncoder.matches(password, account.getPassword());
    }

    @Transactional
    public void delete(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (account instanceof KakaoAccount kakaoAccount) {
            kakaoWebService.disconnect(kakaoAccount.getKakaoId());
        }

        accountRepository.delete(account);
    }
}
