package my.sleepydeveloper.projectcompass.domain.account.service;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.DuplicateNicknameException;
import my.sleepydeveloper.projectcompass.domain.account.exception.NicknameAlreadyExistsException;
import my.sleepydeveloper.projectcompass.domain.account.exception.UsernameAlreadyExistsException;
import my.sleepydeveloper.projectcompass.domain.account.exception.WrongPasswordException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.dto.AccountUpdateCondition;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.context.annotation.ComponentScan.*;

@DataJpaTest
@TestInstance(TestInstance. Lifecycle.PER_CLASS)
class AccountServiceTest {

    @Autowired
    AccountRepository accountRepository;

    PasswordEncoder passwordEncoder;

    AccountService accountService;

    @BeforeAll
    public void init() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        accountService = new AccountService(accountRepository, passwordEncoder);
    }

    @Test
    void save_모든조건통과_저장된Accunt() throws Exception {
        // Given
        accountRepository.save(new Account(username, password, nickname, userRole));

        // When
        String newUsername = "newUsername";
        String newPassword = "newPassword";
        String newNickname = "newNickname";
        Account savedAccount = accountRepository.save(new Account(newUsername, newPassword, newNickname, userRole));

        // Then
        assertThat(savedAccount.getUsername()).isEqualTo(newUsername);
        assertThat(savedAccount.getPassword()).isEqualTo(newPassword);
        assertThat(savedAccount.getNickname()).isEqualTo(newNickname);
        assertThat(savedAccount.getRole()).isEqualTo(userRole);
    }

    @Test
    void save_중복된Username저장_오류발생() throws Exception {
        // Given
        accountRepository.save(new Account(username, password, nickname, userRole));

        // When
        // Then
        String newNickname = "newNickname";
        assertThatThrownBy(() -> accountService.save(new Account(username, password, newNickname, userRole)))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    void save_중복된Nickname저장_오류발생() throws Exception {
        // Given
        accountRepository.save(new Account(username, password, nickname, userRole));

        // When
        // Then
        String newUsername = "newUsername";
        assertThatThrownBy(() -> accountService.save(new Account(newUsername, password, nickname, userRole)))
                .isInstanceOf(NicknameAlreadyExistsException.class);
    }


    @Test
    void updateProfile_모든필드업데이트_성공() throws Exception {
        // Given
        Account account = accountRepository.save(new Account(username, passwordEncoder.encode(password), nickname, userRole));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        AccountUpdateCondition updateCondition = AccountUpdateCondition.builder()
                .nickname(newNickname)
                .password(newPassword)
                .originalPassword(password)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        assertThat(account.getNickname()).isEqualTo(newNickname);
        assertThat(account.getPassword()).isNotEqualTo(passwordEncoder.encode(password));
    }

    @Test
    void updateProfile_모든필드NULL_업데이트안함() throws Exception {
        // Given
        Account account = accountRepository.save(new Account(username, passwordEncoder.encode(password), nickname, userRole));
        AccountUpdateCondition updateCondition = AccountUpdateCondition.builder()
                .password(null)
                .password(null)
                .originalPassword(password)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        assertThat(account.getNickname()).isEqualTo(account.getNickname());
        assertThat(account.getPassword()).isEqualTo(account.getPassword());
    }

    @Test
    void updateProfile_다른originalPassword_오류발생() throws Exception {
        // Given
        Account account = accountRepository.save(new Account(username, passwordEncoder.encode(password), nickname, userRole));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        AccountUpdateCondition updateCondition = AccountUpdateCondition.builder()
                .nickname(newNickname)
                .password(newPassword)
                .originalPassword("fail")
                .build();

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateProfile(account, updateCondition))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    void updateProfile_같은nickname존재_오류발생() throws Exception {
        // Given
        String existingUsername = "existingUsername";
        String existingPassword = "existingPassword";
        String existingNickname = "existingNickname";
        Account existingAccount = accountRepository.save(new Account(existingUsername, passwordEncoder.encode(existingPassword), existingNickname, userRole));

        Account updateAccount = accountRepository.save(new Account(username, passwordEncoder.encode(password), nickname, userRole));

        String newPassword = "newPassword";
        AccountUpdateCondition updateCondition = AccountUpdateCondition.builder()
                .nickname(existingNickname)
                .password(newPassword)
                .originalPassword(password)
                .build();

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateProfile(updateAccount, updateCondition))
                .isInstanceOf(DuplicateNicknameException.class);
    }

    @Test
    void delete_존재하는Account_성공() throws Exception {
        // Given
        Account account = accountRepository.save(new Account(username, passwordEncoder.encode(password), nickname, userRole));

        // When
        // Then
        accountService.delete(account);
    }

}