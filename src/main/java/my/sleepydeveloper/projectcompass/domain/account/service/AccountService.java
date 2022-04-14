package my.sleepydeveloper.projectcompass.domain.account.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.*;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.condition.AccountUpdateCond;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    private void checkDuplicationExists(Account account) {
        if (isExistsUsername(account)) {
            throw new AccountUsernameAlreadyExistsException(ErrorCode.ACCOUNT_USERNAME_EXISTS);
        }
        if (isExistsNickname(account.getNickname())) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }
    }

    public boolean isExistsNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

    private boolean isExistsUsername(Account account) {
        return accountRepository.existsByUsername(account.getUsername());
    }

    @Transactional
    public Account save(Account account) {
        checkDuplicationExists(account);
        return accountRepository.save(account);
    }

    @Transactional
    public void updateProfile(Account account, AccountUpdateCond accountUpdateCondition) {

        // 카카오 로그인은 비밀번호를 검수불가
//        if (!passwordEncoder.matches(
//                accountUpdateCondition.getOriginalPassword(),
//                account.getPassword())) {
//            throw new WrongPasswordException(ErrorCode.ACCOUNT_WRONG_PASSWORD);
//        }

        Account findAccount = accountRepository.findById(account.getId()).orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (accountUpdateCondition.getNickname() != null) {
            if (findAccount.getNickname() != null && findAccount.getNickname().equals(accountUpdateCondition.getNickname())) {
                accountUpdateCondition.setNickname(null);
            } else if (accountRepository.existsByNickname(accountUpdateCondition.getNickname())) {
                throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
            }
        }

        if (accountUpdateCondition.getPassword() != null) {
            accountUpdateCondition.setPassword(passwordEncoder.encode(accountUpdateCondition.getPassword()));
        }

        findAccount.updateProfile(accountUpdateCondition.getNickname(), accountUpdateCondition.getPassword(), null);
    }


    @Transactional
    public void delete(Account account) {
        accountRepository.delete(account);
    }

    @Transactional
    public void updateNickname(Account account, String nickname) {
        Account findAccount = accountRepository.findById(account.getId()).orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        findAccount.updateNickname(nickname);
    }
}
