package com.projectboated.backend.account.account.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNicknameAlreadyExistsException;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.utils.base.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Account(nickname) : Service 단위 테스트")
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

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        // When
        // Then
        String newNickname = "newNickname";
        nicknameService.updateNickname(account.getId(), newNickname);

        verify(accountRepository).findById(account.getId());
    }

    @Test
    void updateNickname_account찾을수없음_예외발생() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> nicknameService.updateNickname(account.getId(), "newNickname"))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(account.getId());
    }

    @Test
    void updateNickname_중복된nickname_예외발생() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        String newNickname = "newNickname";

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(accountRepository.existsByNickname(newNickname)).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> nicknameService.updateNickname(account.getId(), newNickname))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);

        verify(accountRepository).findById(account.getId());
    }

    @Test
    void updateNickname_같은닉네임주어짐_정상update() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        // When
        nicknameService.updateNickname(account.getId(), NICKNAME);

        // Then
        assertThat(account.getNickname()).isEqualTo(NICKNAME);

        verify(accountRepository).findById(account.getId());
    }

    @Test
    void updateNickname_account의nickname이null일때_업데이트성공() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        // When
        nicknameService.updateNickname(account.getId(), NICKNAME);

        // Then
        assertThat(account.getNickname()).isEqualTo(NICKNAME);

        verify(accountRepository).findById(account.getId());
    }

    @Test
    void existsByNickname_nickname이null로주어진경우_assert예외발생() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> nicknameService.existsByNickname(null))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    void existsByNickname_nickname이존재하는경우_return_true() {
        // Given
        String nickname = "nickname";

        when(accountRepository.existsByNickname(nickname)).thenReturn(true);

        // When
        boolean result = nicknameService.existsByNickname(nickname);

        // Then
        assertThat(result).isTrue();

        verify(accountRepository).existsByNickname(nickname);
    }

    @Test
    void existsByNickname_nickname이존재하지않는경우_return_false() {
        // Given
        String nickname = "nickname";

        when(accountRepository.existsByNickname(any())).thenReturn(false);

        // When
        boolean result = nicknameService.existsByNickname(nickname);

        // Then
        assertThat(result).isFalse();

        verify(accountRepository).existsByNickname(nickname);
    }

}