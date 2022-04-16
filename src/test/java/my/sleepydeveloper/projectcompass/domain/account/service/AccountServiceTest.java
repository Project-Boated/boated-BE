package my.sleepydeveloper.projectcompass.domain.account.service;

import my.sleepydeveloper.projectcompass.common.basetest.UnitTest;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;
import my.sleepydeveloper.projectcompass.domain.account.exception.*;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.repository.KakaoAccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.condition.AccountUpdateCond;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class AccountServiceTest extends UnitTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KakaoAccountRepository kakaoAccountRepository;

    private AccountService accountService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        accountService = new AccountService(accountRepository, passwordEncoder);
    }

    @Test
    void save_존재하지않는Username_AND_존재하지않는Nickname_으로저장_성공() throws Exception {
        // Given
        // When
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void save_중복된Username저장_오류발생() throws Exception {
        // Given
        accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        // When
        // Then
        String newNickname = "newNickname";
        assertThatThrownBy(() -> accountService.save(new Account(USERNAME, PASSWORD, newNickname, PROFILE_IMAGE_URL, ROLES)))
                .isInstanceOf(AccountUsernameAlreadyExistsException.class);
    }

    @Test
    void save_중복된Nickname저장_오류발생() throws Exception {
        // Given
        accountRepository.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        // When
        // Then
        String newUsername = "newUsername";
        assertThatThrownBy(() -> accountService.save(new Account(newUsername, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES)))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

    @Test
    void findById_존재하는Id로조회_조회성공() {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);
        accountService.save(account);

        // When
        Account findAccount = accountService.findById(account.getId());

        // Then
        AssertionsForClassTypes.assertThat(findAccount.getUsername()).isEqualTo(account.getUsername());
        AssertionsForClassTypes.assertThat(findAccount.getPassword()).isEqualTo(account.getPassword());
        AssertionsForClassTypes.assertThat(findAccount.getNickname()).isEqualTo(account.getNickname());
        AssertionsForClassTypes.assertThat(findAccount.getProfileImageUrl()).isEqualTo(account.getProfileImageUrl());
        AssertionsForClassTypes.assertThat(findAccount.getRoles()).isEqualTo(account.getRoles());
    }

    @Test
    void findById_존재하지않는Id로조회_오류발생() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> accountService.findById(123123L)).isInstanceOf(AccountNotFoundException.class);
    }


    @Test
    void updateProfile_모든필드업데이트_성공() throws Exception {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .password(newPassword)
                .profileImageUrl(newProfileImageUrl)
                .originalPassword(PASSWORD)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(newPassword, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(newNickname);
        AssertionsForClassTypes.assertThat(account.getProfileImageUrl()).isEqualTo(newProfileImageUrl);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_카카오Account_모든필드업데이트_성공() throws Exception {
        // Given
        KakaoAccount kakaoAccount = new KakaoAccount(123L, ROLES, PROFILE_IMAGE_URL);
        kakaoAccount.updateNickname(NICKNAME);
        kakaoAccountRepository.save(kakaoAccount);

        String newNickname = "newNickname";
        String newProfileImageUrl = "newProfileImageUrl";
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .profileImageUrl(newProfileImageUrl)
                .build();

        // When
        accountService.updateProfile(kakaoAccount, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(kakaoAccount.getUsername()).isNull();
        AssertionsForClassTypes.assertThat(kakaoAccount.getPassword()).isNull();
        AssertionsForClassTypes.assertThat(kakaoAccount.getNickname()).isEqualTo(newNickname);
        AssertionsForClassTypes.assertThat(kakaoAccount.getProfileImageUrl()).isEqualTo(newProfileImageUrl);
        AssertionsForClassTypes.assertThat(kakaoAccount.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_모든필드NULL_업데이트안함() throws Exception {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(null)
                .password(null)
                .profileImageUrl(null)
                .originalPassword(PASSWORD)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_OriginalPassword없음_오류발생() throws Exception {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .password(newPassword)
                .profileImageUrl(newProfileImageUrl)
                .originalPassword(null)
                .build();

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateProfile(account, updateCondition))
                .isInstanceOf(AccountPasswordWrong.class);
    }

    @Test
    void updateProfile_다른originalPassword_오류발생() throws Exception {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .password(newPassword)
                .profileImageUrl(newProfileImageUrl)
                .originalPassword("failurePassword")
                .build();

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateProfile(account, updateCondition))
                .isInstanceOf(AccountPasswordWrong.class);
    }

    @Test
    void updateProfile_존재하지않는AccountId_오류발생() throws Exception {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .password(newPassword)
                .profileImageUrl(newProfileImageUrl)
                .originalPassword(PASSWORD)
                .build();

        // When
        account.updateId(123123L);

        // Then
        assertThatThrownBy(() -> accountService.updateProfile(account, updateCondition))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void updateProfile_같은nickname으로업데이트_성공() throws Exception {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(NICKNAME)
                .password(newPassword)
                .profileImageUrl(newProfileImageUrl)
                .originalPassword(PASSWORD)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(newPassword, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImageUrl()).isEqualTo(newProfileImageUrl);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_같은nickname존재_오류발생() throws Exception {
        // Given
        String account1Username = "account1Username";
        String account1Nickname = "account1Nickname";
        Account account1 = accountService.save(new Account(account1Username, PASSWORD, account1Nickname, PROFILE_IMAGE_URL, ROLES));

        String account2Username = "account2Username";
        String account2Nickname = "account2Nickname";
        Account account2 = accountService.save(new Account(account2Username, PASSWORD, account2Nickname, PROFILE_IMAGE_URL, ROLES));

        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(account1Nickname)
                .password(newPassword)
                .profileImageUrl(newProfileImageUrl)
                .originalPassword(PASSWORD)
                .build();

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateProfile(account2, updateCondition))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

    @Test
    void delete_Account삭제_성공() throws Exception {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        // When
        accountService.delete(account);

        // Then
        // delete후 find하면 없어야 정상
        assertThatThrownBy(() -> accountService.findById(account.getId()))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void updateNickname_새로운Nickname으로변경_변경성공() {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        // When
        String newNickname = "newNickname";
        accountService.updateNickname(account, newNickname);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(newNickname);
        AssertionsForClassTypes.assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_존재하지않는AccountId_오류발생() {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));
        account.updateId(12345L);

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateNickname(account, NICKNAME));
    }

    @Test
    void updateNickname_같은Nickname으로변경_변경없음() {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        // When
        accountService.updateNickname(account, NICKNAME);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_NULL로변경_변경없음() {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));

        // When
        accountService.updateNickname(account, null);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_이미존재하는Nickname으로변경_오류발생() {
        // Given
        String account1Username = "account1Username";
        String account1Nickname = "account1Nickname";
        Account account1 = accountService.save(new Account(account1Username, PASSWORD, account1Nickname, PROFILE_IMAGE_URL, ROLES));

        String account2Username = "account2Username";
        String account2Nickname = "account2Nickname";
        Account account2 = accountService.save(new Account(account2Username, PASSWORD, account2Nickname, PROFILE_IMAGE_URL, ROLES));

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateNickname(account2, account1Nickname))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

}