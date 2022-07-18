package com.projectboated.backend.domain.account.account.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNicknameAlreadyExistsException;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountNicknameServiceTest extends ServiceTest {

    @InjectMocks
    AccountNicknameService nicknameService;

    @Mock
    AccountRepository accountRepository;

    @Test
    void updateNickname_nickname주어짐_업데이트성공() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));

        // When
        // Then
        String newNickname = "newNickname";
        nicknameService.updateNickname(account.getId(), newNickname);
    }

    @Test
    void updateNickname_account찾을수없음_예외발생() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> nicknameService.updateNickname(account.getId(), "newNickname"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void updateNickname_중복된nickname_예외발생() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(accountRepository.existsByNickname(any())).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> nicknameService.updateNickname(account.getId(), "newNickname"))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

    @Test
    void existsByNickname_nickname이존재하는경우_return_true() {
        // Given
        when(accountRepository.existsByNickname(any())).thenReturn(true);

        // When
        boolean result = nicknameService.existsByNickname("nickname");

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByNickname_nickname이존재하지않는경우_return_false() {
        // Given
        when(accountRepository.existsByNickname(any())).thenReturn(false);

        // When
        boolean result = nicknameService.existsByNickname("nickname");

        // Then
        assertThat(result).isFalse();
    }

}