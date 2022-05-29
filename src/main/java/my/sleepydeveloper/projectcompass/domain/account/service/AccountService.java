package my.sleepydeveloper.projectcompass.domain.account.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;
import my.sleepydeveloper.projectcompass.domain.common.exception.CommonIOException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.*;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.condition.AccountUpdateCond;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.UploadFileProfileImage;
import my.sleepydeveloper.projectcompass.domain.profileimage.service.AwsS3ProfileImageService;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import my.sleepydeveloper.projectcompass.security.service.KakaoWebService;
import my.sleepydeveloper.projectcompass.utils.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static my.sleepydeveloper.projectcompass.utils.StringUtils.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoWebService kakaoWebService;
    private final AwsS3ProfileImageService awsS3ProfileService;
    private final AccountNicknameService nicknameService;

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

        account.updatePassword(passwordEncoder.encode(account.getPassword()));
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
            findAccount.updateProfileImage(uploadFileProfileImage);
        }

        findAccount.updateProfile(updateCond.getNickname(), updateCond.getNewPassword());
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
}
