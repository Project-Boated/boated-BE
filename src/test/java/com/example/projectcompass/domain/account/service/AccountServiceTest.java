package com.example.projectcompass.domain.account.service;

import com.example.projectcompass.domain.account.entity.Account;
import com.example.projectcompass.domain.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;

    @Test
    @DisplayName("save 중복된 Username")
    void save_중복된_username() throws Exception {
        // Given
        String username = "username";
        String password = "password";
        String role = "ROLE_USER";
        Mockito.when(accountRepository.existsByUsername(username)).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> accountService.save(new Account(username, password, role)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("save 중복X Username")
    void save_중복X_username() throws Exception {
        // Given
        String username = "username";
        String password = "password";
        String role = "ROLE_USER";
        Account account = new Account(username, password, role);

        Mockito.when(accountRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(accountService.save(account)).thenReturn(account);

        // When
        Account savedAccount = accountService.save(account);

        // Then
        assertThat(savedAccount.getUsername()).isEqualTo(username);
        assertThat(savedAccount.getPassword()).isEqualTo(password);
        assertThat(savedAccount.getRole()).isEqualTo(role);
    }

}