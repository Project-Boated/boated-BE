package my.sleepydeveloper.projectcompass.domain.account.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.*;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.dto.AccountUpdateCondition;
import my.sleepydeveloper.projectcompass.domain.account.value.AccountProfile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public AccountProfile findProfileByAccount(Account account) {
        Account findAccount = accountRepository.findByUsername(account.getUsername())
                .orElseThrow(() -> new NotFoundAccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        return new AccountProfile(findAccount);
    }

    @Transactional
    public void updateProfile(AccountUpdateCondition accountUpdateCondition) {
        Account account = accountRepository.findById(accountUpdateCondition.getAccountId())
                .orElseThrow(() -> new NotFoundAccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!passwordEncoder.matches(
                accountUpdateCondition.getOriginalPassword(),
                account.getPassword())) {
            throw new WrongPasswordException(ErrorCode.ACCOUNT_WRONG_PASSWORD);
        }

        if(accountUpdateCondition.getNickname() != null) {
            if (accountRepository.existsByNickname(accountUpdateCondition.getNickname())) {
                throw new DuplicateNicknameException(ErrorCode.ACCOUNT_DUPLICATE_NICKNAME);
            }
        }

        account.updateProfile(accountUpdateCondition.getNickname(), passwordEncoder.encode(accountUpdateCondition.getPassword()));
    }

    @Transactional
    public void delete(Account account) {
        if(!accountRepository.existsById(account.getId())) {
            throw new NotFoundAccountException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        accountRepository.delete(account);
    }
}
