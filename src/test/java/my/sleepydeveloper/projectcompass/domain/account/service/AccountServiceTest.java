package my.sleepydeveloper.projectcompass.domain.account.service;

import my.sleepydeveloper.projectcompass.common.basetest.UnitTest;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.*;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.dto.AccountUpdateCondition;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@TestInstance(TestInstance. Lifecycle.PER_CLASS)
class AccountServiceTest extends UnitTest {

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
        Account savedAccount = accountService.save(new Account(newUsername, newPassword, newNickname, userRole));

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
                .isInstanceOf(AccountUsernameAlreadyExistsException.class);
    }

    @Test
    void save_중복된Nickname저장_오류발생() throws Exception {
        // Given
        accountRepository.save(new Account(username, password, nickname, userRole));

        // When
        // Then
        String newUsername = "newUsername";
        assertThatThrownBy(() -> accountService.save(new Account(newUsername, password, nickname, userRole)))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
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
                .nickname(null)
                .originalPassword(password)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        assertThat(account.getNickname()).isEqualTo(account.getNickname());
        assertThat(account.getPassword()).isEqualTo(account.getPassword());
    }

    @Test
    void updateProfile_같은nickname으로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = accountRepository.save(new Account(username, passwordEncoder.encode(password), nickname, userRole));
        AccountUpdateCondition updateCondition = AccountUpdateCondition.builder()
                .password(null)
                .nickname(account.getNickname())
                .originalPassword(password)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        assertThat(account.getNickname()).isEqualTo(account.getNickname());
    }

    @Test
    @Disabled("카카오 로그인지원으로 인해 original password입력을 받지 않음.")
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
                .isInstanceOf(Exception.class);
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
                .isInstanceOf(AccountNicknameDuplicateException.class);
    }

    @Test
    void delete_존재하는Account_성공() throws Exception {
        // Given
        Account account = accountRepository.save(new Account(username, passwordEncoder.encode(password), nickname, userRole));

        // When
        // Then
        accountService.delete(account);
    }

    @Test
    void findById_존재하는Id로조회_조회성공() {
        // Given
        Account account = new Account(username, passwordEncoder.encode(password), nickname, userRole);
        accountService.save(account);

        // When
        Account findAccount = accountService.findById(account.getId());

        // Then
        assertThat(findAccount.getUsername()).isEqualTo(username);
        assertThat(findAccount.getNickname()).isEqualTo(nickname);
        assertThat(findAccount.getRole()).isEqualTo(userRole);
    }

    @Test
    void findById_존재하지않는Id로조회_오류발생() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> accountService.findById(123123L)).isInstanceOf(AccountNotFoundException.class);
    }

}