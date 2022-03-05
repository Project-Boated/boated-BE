package my.sleepydeveloper.projectcompass.common.config;

import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestConfigRegisterAccountService {
    @Autowired
    AccountRepository accountRepository;

    @Bean
    public AccountService accountService(AccountRepository accountRepository) {
        return new AccountService(accountRepository, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AccountTestUtils accountTestUtils() {
        return new AccountTestUtils(accountService(accountRepository), passwordEncoder());
    }
}