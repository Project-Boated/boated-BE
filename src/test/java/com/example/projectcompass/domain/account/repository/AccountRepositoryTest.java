package com.example.projectcompass.domain.account.repository;

import com.example.projectcompass.domain.account.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    @DisplayName("findByUsername_정상")
    void findByUsername_정상() throws Exception {
        // Given
        String username = "user";
        String password = "1111";
        createAccount(username, password);

        // When
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Account를 찾을 수 없습니다."));

        // Then
        assertThat(account.getUsername()).isEqualTo(username);
        assertThat(account.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("findByUsername_실패")
    void findByUsername_실패() throws Exception {
        // Given
        String username = "user";
        String password = "1111";
        createAccount(username, password);

        // When
        Optional<Account> account = accountRepository.findByUsername("fail");

        // Then
        assertThatThrownBy(() -> account.get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("유저이름으로_Account_존재확인_없음")
    void existsByUsername_true() throws Exception {
        // Given
        String username = "username";
        String password = "1111";
        createAccount(username, password);

        // When
        boolean existsByUsername = accountRepository.existsByUsername("fail");

        // Then
        assertThat(existsByUsername).isFalse();
    }

    @Test
    @DisplayName("유저이름으로_Account_존재확인_실패")
    void existsByUsername_false() throws Exception {
        // Given
        String username = "username";
        String password = "1111";
        createAccount(username, password);

        // When
        boolean existsByUsername = accountRepository.existsByUsername(username);

        // Then
        assertThat(existsByUsername).isTrue();
    }

    private Account createAccount(String username, String password) {
        return accountRepository.save(new Account(username, password, "ROLE_USER"));
    }

}