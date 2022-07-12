package com.projectboated.backend.domain.account.account.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Account : Service 단위 테스트")
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Test
    void findById_존재하는Id_return_Account1개() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(ACCOUNT));

        // When
        Account result = accountService.findById(1L);

        // Then
        assertThat(result).isEqualTo(ACCOUNT);

        verify(accountRepository).findById(1L);
    }

    @Test
    void findById_존재하지않는Id_예외발생() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> accountService.findById(1L))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(1L);
    }


}