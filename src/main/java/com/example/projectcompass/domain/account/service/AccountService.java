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
        return accountRepository.save(account);
    }
}
