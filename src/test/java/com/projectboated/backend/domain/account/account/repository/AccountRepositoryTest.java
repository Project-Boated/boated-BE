package com.projectboated.backend.domain.account.account.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicAccountData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Account : Persistence 단위 테스트")
class AccountRepositoryTest extends RepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findByUsername_username이같은account존재_return_account() {
        // Given
        insertDefaultAccount();

        // When
        Optional<Account> byUsername = accountRepository.findByUsername(USERNAME);

        // Then
        assertThat(byUsername).isPresent();
        assertThat(byUsername.get().getUsername()).isEqualTo(USERNAME);
        assertThat(byUsername.get().getNickname()).isEqualTo(NICKNAME);
        assertThat(byUsername.get().getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void findByUsername_username이같은account없음_return_empty() {
        // Given
        insertDefaultAccount();

        // When
        Optional<Account> byUsername = accountRepository.findByUsername("fail");

        // Then
        assertThat(byUsername).isEmpty();
    }

    @Test
    void findByNickname_nickname이같은account존재_return_account() {
        // Given
        insertDefaultAccount();

        // When
        Optional<Account> byNickname = accountRepository.findByNickname(NICKNAME);

        // Then
        assertThat(byNickname).isPresent();
        assertThat(byNickname.get().getUsername()).isEqualTo(USERNAME);
        assertThat(byNickname.get().getNickname()).isEqualTo(NICKNAME);
        assertThat(byNickname.get().getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void findByNickname_nickname이같은account없음_return_empty() {
        // Given
        insertDefaultAccount();

        // When
        Optional<Account> byNickname = accountRepository.findByNickname("fail");

        // Then
        assertThat(byNickname).isEmpty();
    }

    @Test
    void existsByUsername_username인account존재_return_true() {
        // Given
        insertDefaultAccount();

        // When
        boolean result = accountRepository.existsByUsername(USERNAME);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByUsername_username인account없음_return_false() {
        // Given
        insertDefaultAccount();

        // When
        boolean result = accountRepository.existsByUsername("fail");

        // Then
        assertThat(result).isFalse();
    }
    @Test
    void existsByNickname_nickname인account존재_return_true() {
        // Given
        insertDefaultAccount();

        // When
        boolean result = accountRepository.existsByNickname(NICKNAME);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByNickname_nickname인account없음_return_false() {
        // Given
        insertDefaultAccount();

        // When
        boolean result = accountRepository.existsByNickname("fail");

        // Then
        assertThat(result).isFalse();
    }

    private void insertDefaultAccount() {
        accountRepository.save(ACCOUNT);
    }

    private void insertAccount(Account account) {
        accountRepository.save(account);
    }

}