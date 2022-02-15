package com.example.projectcompass.domain.account.service;

import com.example.projectcompass.domain.account.entity.Account;
import com.example.projectcompass.domain.account.repository.AccountRepository;
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
        boolean isExistsUsername = accountRepository.existsByUsername(account.getUsername());
        if (isExistsUsername) {
            throw new IllegalArgumentException("예외발생");
        }
    }
}
