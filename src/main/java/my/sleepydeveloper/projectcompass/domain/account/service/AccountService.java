package my.sleepydeveloper.projectcompass.domain.account.service;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.NicknameAlreadyExistsException;
import my.sleepydeveloper.projectcompass.domain.account.exception.UsernameAlreadyExistsException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account save(Account account) {
        checkDuplicationExists(account);
        return accountRepository.save(account);
    }

    private void checkDuplicationExists(Account account) {
        if (isExistsUsername(account)) {
            throw new UsernameAlreadyExistsException(ErrorCode.ACCOUNT_EXISTS_USERNAME);
        }
        if (isExistsNickname(account)) {
            throw new NicknameAlreadyExistsException(ErrorCode.ACCOUNT_EXISTS_NICKNAME);
        }
    }

    private boolean isExistsNickname(Account account) {
        return accountRepository.existsByNickname(account.getUsername());
    }

    private boolean isExistsUsername(Account account) {
        return accountRepository.existsByUsername(account.getUsername());
    }
}
