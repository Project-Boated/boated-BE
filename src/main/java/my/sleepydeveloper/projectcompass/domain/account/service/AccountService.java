package my.sleepydeveloper.projectcompass.domain.account.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.*;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.condition.AccountUpdateCond;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import my.sleepydeveloper.projectcompass.domain.uploadfile.repository.UploadFileRepository;
import my.sleepydeveloper.projectcompass.security.service.KakaoWebService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final UploadFileRepository uploadFileRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoWebService kakaoWebService;

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Transactional
    public Account save(Account account) {
        checkDuplicationExists(account);
        passwordEncode(account);
        return accountRepository.save(account);
    }

    private void checkDuplicationExists(Account account) {
        if (isExistsUsername(account)) {
            throw new AccountUsernameAlreadyExistsException(ErrorCode.ACCOUNT_USERNAME_EXISTS);
        }
        if (isExistsNickname(account.getNickname())) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }
    }

    private boolean isExistsUsername(Account account) {
        return accountRepository.existsByUsername(account.getUsername());
    }

    public boolean isExistsNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

    private void passwordEncode(Account account) {
        account.updatePassword(passwordEncoder.encode(account.getPassword()));
    }

    @Transactional
    public void updateProfile(Account account, AccountUpdateCond accountUpdateCondition) {

        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!(account instanceof KakaoAccount)) {
            if (!checkVaildPassword(findAccount, accountUpdateCondition.getOriginalPassword())) {
                throw new AccountPasswordWrong(ErrorCode.ACCOUNT_PASSWORD_WRONG);
            }
        }

        if (!hasSameNickname(accountUpdateCondition, findAccount) &&
                checkDuplicatedNickname(accountUpdateCondition.getNickname())) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }

        if (accountUpdateCondition.getNewPassword() != null) {
            accountUpdateCondition.setNewPassword(passwordEncoder.encode(accountUpdateCondition.getNewPassword()));
        }

        findAccount.updateProfile(accountUpdateCondition.getNickname(),
                accountUpdateCondition.getNewPassword(),
                accountUpdateCondition.getProfileImageFile());
    }

    private boolean hasSameNickname(AccountUpdateCond accountUpdateCondition, Account findAccount) {
        return Objects.equals(findAccount.getNickname(), accountUpdateCondition.getNickname());
    }

    private boolean checkVaildPassword(Account account, String password) {
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

    @Transactional
    public void updateNickname(Account account, String nickname) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!Objects.equals(account.getNickname(), nickname)
                && checkDuplicatedNickname(nickname)) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }

        findAccount.updateNickname(nickname);
    }

    private boolean checkDuplicatedNickname(String nickname) {
        return nickname != null && accountRepository.existsByNickname(nickname);
    }

    @Transactional
    public void updateProfileImage(Account account, UploadFile uploadFile) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        findAccount.updateProfileImageFile(uploadFile);
    }
}
