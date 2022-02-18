package my.sleepydeveloper.projectcompass.test.utils;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
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

    public Account signUpUser(String username, String password, String nickname) {
        return signUp(username, password, nickname, "ROLE_USER");
    }

    public Account signUp(String username, String password, String nickname, String role) {
        return accountService.save(new Account(username, passwordEncoder.encode(password), nickname, role));
    }
}
