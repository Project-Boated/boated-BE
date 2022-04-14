package my.sleepydeveloper.projectcompass.domain.account.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.*;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.dto.AccountUpdateCondition;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Account save(Account account) {
        checkDuplicationExists(account);
        return accountRepository.save(account);
    }

    private void checkDuplicationExists(Account account) {
        if (isExistsUsername(account)) {
            throw new AccountUsernameAlreadyExistsException(ErrorCode.ACCOUNT_EXISTS_USERNAME);
        }
        if (isExistsNickname(account.getNickname())) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_EXISTS_NICKNAME);
        }
    }

    public boolean isExistsNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

    private boolean isExistsUsername(Account account) {
        return accountRepository.existsByUsername(account.getUsername());
    }

    @Transactional
    public void updateProfile(Account account, AccountUpdateCondition accountUpdateCondition) {

        // 카카오 로그인은 비밀번호를 검수불가
//        if (!passwordEncoder.matches(
//                accountUpdateCondition.getOriginalPassword(),
//                account.getPassword())) {
//            throw new WrongPasswordException(ErrorCode.ACCOUNT_WRONG_PASSWORD);
//        }

        Account findAccount = accountRepository.findById(account.getId()).orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if(accountUpdateCondition.getNickname() != null) {
            if(findAccount.getNickname()!=null && findAccount.getNickname().equals(accountUpdateCondition.getNickname())) {
                 accountUpdateCondition.setNickname(null);
            }
            else if (accountRepository.existsByNickname(accountUpdateCondition.getNickname())) {
                throw new AccountNicknameDuplicateException(ErrorCode.ACCOUNT_DUPLICATE_NICKNAME);
            }
        }

        if (accountUpdateCondition.getPassword() != null) {
            accountUpdateCondition.setPassword(passwordEncoder.encode(accountUpdateCondition.getPassword()));
        }

        findAccount.updateProfile(accountUpdateCondition.getNickname(), accountUpdateCondition.getPassword());
    }

    @Transactional
    public void delete(Account account) {
        accountRepository.delete(account);
    }
    
    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
    }
}
