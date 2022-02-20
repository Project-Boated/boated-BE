package my.sleepydeveloper.projectcompass.domain.account.service;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.*;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.dto.AccountUpdateCondition;
import my.sleepydeveloper.projectcompass.domain.account.value.AccountProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AccountService accountService;

    private final String username = "username";
    private final String nickname = "nickname";
    private final String password = "1111";
    private final String role = "ROLE_USER";
    private final String newPassword = "newPassword";
    private String newNickname = "newNickname";

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
        assertThat(savedAccount.getNickname()).isEqualTo(nickname);
        assertThat(savedAccount.getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("Account로_AccountProfile_찾기_정상")
    void findAccountProfileByAccount_정상() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));

        // When
        AccountProfile accountProfile = accountService.findProfileByAccount(account);

        // Then
        assertThat(accountProfile.getUsername()).isEqualTo(username);
        assertThat(accountProfile.getNickname()).isEqualTo(nickname);
        assertThat(accountProfile.getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("Account로_AccountProfile_찾기_실패")
    void findAccountProfileByAccount_실패() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        // When
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> accountService.findProfileByAccount(account))
                .isInstanceOf(NotFoundAccountException.class);
    }

    @Test
    @DisplayName("UpdateProfile에서 AccountId로 Account 찾기 실패")
    void updateProfile_account_id_찾기실패() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        // When
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> accountService.updateProfile(createAccountProfileCondition(account)))
                .isInstanceOf(NotFoundAccountException.class);
    }


    @Test
    @DisplayName("UpdateProfile_성공_모든필드_update")
    void updateProfile_성공_모든필드업데이트() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        // When
        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn(newPassword);
        when(accountRepository.existsByNickname(any())).thenReturn(false);

        accountService.updateProfile(createAccountProfileCondition(account));

        // Then
        assertThat(account.getNickname()).isEqualTo(newNickname);
        assertThat(account.getPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("UpdateProfile_성공_password만_업데이트")
    void updateProfile_성공_password만() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        // When
        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn(newPassword);

        accountService.updateProfile(new AccountUpdateCondition(account.getId(), null, account.getPassword(), newPassword));

        // Then
        assertThat(account.getNickname()).isEqualTo(nickname);
        assertThat(account.getPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("UpdateProfile에서 original password 검증 실패")
    void updateProfile_original_password_검증_실패() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        // When
        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // Then
        assertThatThrownBy(() -> accountService.updateProfile(createAccountProfileCondition(account)))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    @DisplayName("UpdateProfile에서 같은 nickname인 account존재_실패")
    void updateProfile_같은_nickname인_account_존재_실패() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        // When
        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(accountRepository.existsByNickname(any())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> accountService.updateProfile(createAccountProfileCondition(account)))
                .isInstanceOf(DuplicateNicknameException.class);
    }

    @Test
    @DisplayName("delete 성공")
    void delete_성공() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        when(accountRepository.existsById(any())).thenReturn(true);

        // When
        // Then
        accountService.delete(account);
    }

    @Test
    @DisplayName("delete_실패_잘못된_account")
    void delete_실패_잘못된_account() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        when(accountRepository.existsById(any())).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> accountService.delete(account))
                .isInstanceOf(NotFoundAccountException.class);
    }

    private AccountUpdateCondition createAccountProfileCondition(Account account) {
        return new AccountUpdateCondition(account.getId(), newNickname, password, newPassword);
    }

}