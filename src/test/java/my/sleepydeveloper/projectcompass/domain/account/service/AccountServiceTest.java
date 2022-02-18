package my.sleepydeveloper.projectcompass.domain.account.service;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.NicknameAlreadyExistsException;
import my.sleepydeveloper.projectcompass.domain.account.exception.UsernameAlreadyExistsException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;

    private final String username = "username";
    private final String nickname = "nickname";
    private final String password = "1111";
    private final String role = "ROLE_USER";

    @Test
    @DisplayName("save 중복된 Username")
    void save_중복된_username() throws Exception {
        // Given
        when(accountRepository.existsByUsername(any())).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> accountService.save(new Account(username, password, nickname, role)))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    @DisplayName("save 중복된 nickname")
    void save_중복된_nickname() throws Exception {
        // Given
        when(accountRepository.existsByNickname(any())).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> accountService.save(new Account(username, password, nickname, role)))
                .isInstanceOf(NicknameAlreadyExistsException.class);
    }

    @Test
    @DisplayName("save 정상 Username, 정상 nickname")
    void save_정상_username_정상_nickname() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        when(accountRepository.existsByUsername(username)).thenReturn(false);
        when(accountRepository.existsByNickname(username)).thenReturn(false);
        when(accountService.save(account)).thenReturn(account);

        // When
        Account savedAccount = accountService.save(account);

        // Then
        assertThat(savedAccount.getUsername()).isEqualTo(username);
        assertThat(savedAccount.getPassword()).isEqualTo(password);
        assertThat(savedAccount.getRole()).isEqualTo(role);
    }

}