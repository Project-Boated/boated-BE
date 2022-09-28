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
import com.projectboated.backend.infra.aws.AwsS3ProfileImageService;
import com.projectboated.backend.infra.kakao.KakaoWebService;
import com.projectboated.backend.uploadfile.entity.UploadFile;
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
                .orElseThrow(AccountNotFoundException::new);
    }

    @Transactional
    public Account save(Account account) {
        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new AccountUsernameAlreadyExistsException();
        }

        if (accountRepository.existsByNickname(account.getNickname())) {
            throw new AccountNicknameAlreadyExistsException();
        }

        account.changePassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Transactional
    public void updateProfile(Long accountId, AccountUpdateCond updateCond) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        if (!checkValidPassword(account, updateCond.getOriginalPassword())) {
            throw new AccountPasswordWrong();
        }

        if (checkValidNickname(updateCond, account)) {
            throw new AccountNicknameAlreadyExistsException();
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
            profileImageRepository.save(uploadFileProfileImage);

            account.changeProfileImage(uploadFileProfileImage);

            awsS3ProfileService.uploadProfileImage(account, uploadFileProfileImage, updateCond.getProfileImageFile());
        }

        account.changeNickname(updateCond.getNickname());
    }

    private boolean checkValidNickname(AccountUpdateCond updateCond, Account account) {
        return updateCond.getNickname() != null &&
                !updateCond.getNickname().equals(account.getNickname()) &&
                accountRepository.existsByNickname(updateCond.getNickname());
    }

    private boolean checkValidPassword(Account account, String password) {
        if (account instanceof KakaoAccount) {
            return true;
        }

        if (password == null) {
            return false;
        }
        return passwordEncoder.matches(password, account.getPassword());
    }

    @Transactional
    public void delete(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        if (account instanceof KakaoAccount kakaoAccount) {
            kakaoWebService.disconnect(kakaoAccount.getKakaoId());
        }

        accountRepository.delete(account);
    }
}
