package com.example.projectcompass.web.account.utils;

import com.example.projectcompass.domain.account.entity.Account;
import com.example.projectcompass.domain.account.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountTestUtils {
    
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public AccountTestUtils(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    public Account signUpUser(String username, String password) {
        return signUp(username, password, "ROLE_USER");
    }

    public Account signUp(String username, String password, String role) {
        return accountService.save(new Account(username, passwordEncoder.encode(password), role));
    }
}
