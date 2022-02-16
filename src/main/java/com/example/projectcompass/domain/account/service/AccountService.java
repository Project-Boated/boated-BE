package com.example.projectcompass.domain.account.service;

import com.example.projectcompass.domain.account.entity.Account;
import com.example.projectcompass.domain.account.exception.UsernameAlreadyExistsException;
import com.example.projectcompass.domain.account.repository.AccountRepository;
import com.example.projectcompass.domain.common.exception.ErrorCode;
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
        checkUsernameExists(account);
        return accountRepository.save(account);
    }

    private void checkUsernameExists(Account account) {
        if (isExistsUsername(account) || isExistsNickname(account)) {
            throw new UsernameAlreadyExistsException(ErrorCode.ACCOUNT_ALREADY_USERNAME_EXISTS);
        }
    }

    private boolean isExistsNickname(Account account) {
        return accountRepository.existsByNickname(account.getUsername());
    }

    private boolean isExistsUsername(Account account) {
        return accountRepository.existsByUsername(account.getUsername());
    }
}
