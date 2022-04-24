package my.sleepydeveloper.projectcompass.domain.account.service;

import my.sleepydeveloper.projectcompass.common.basetest.BaseTest;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;
import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNicknameAlreadyExistsException;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNotFoundException;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountPasswordWrong;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountUsernameAlreadyExistsException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.repository.KakaoAccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.condition.AccountUpdateCond;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import my.sleepydeveloper.projectcompass.domain.uploadfile.repository.UploadFileRepository;
import my.sleepydeveloper.projectcompass.security.service.KakaoWebService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.context.annotation.ComponentScan.Filter;

@DataJpaTest(includeFilters = {
        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = PasswordEncoder.class)
})
@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends BaseTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KakaoAccountRepository kakaoAccountRepository;

    @Autowired
    private UploadFileRepository uploadFileRepository;

    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    KakaoWebService kakaoWebService;

    @BeforeEach
    void beforeEach() {
        accountService = new AccountService(accountRepository,uploadFileRepository, passwordEncoder, kakaoWebService);
    }

    @AfterEach
    void afterEach() {
        PROFILE_IMAGE_FILE.setId(null);
    }

    @Test
    void save_존재하지않는Username_AND_존재하지않는Nickname_으로저장_성공() throws Exception {
        // Given
        // When
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImageFile()).isEqualTo(PROFILE_IMAGE_FILE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void save_중복된Username저장_오류발생() throws Exception {
        // Given
        accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        // When
        // Then
        String newNickname = "newNickname";
        assertThatThrownBy(() -> accountService.save(new Account(USERNAME, PASSWORD, newNickname, PROFILE_IMAGE_FILE, ROLES)))
                .isInstanceOf(AccountUsernameAlreadyExistsException.class);
    }

    @Test
    void save_중복된Nickname저장_오류발생() throws Exception {
        // Given
        accountRepository.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        // When
        // Then
        String newUsername = "newUsername";
        assertThatThrownBy(() -> accountService.save(new Account(newUsername, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES)))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

    @Test
    void findById_존재하는Id로조회_조회성공() {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);
        accountService.save(account);

        // When
        Account findAccount = accountService.findById(account.getId());

        // Then
        AssertionsForClassTypes.assertThat(findAccount.getUsername()).isEqualTo(account.getUsername());
        AssertionsForClassTypes.assertThat(findAccount.getPassword()).isEqualTo(account.getPassword());
        AssertionsForClassTypes.assertThat(findAccount.getNickname()).isEqualTo(account.getNickname());
        AssertionsForClassTypes.assertThat(findAccount.getProfileImageFile()).isEqualTo(account.getProfileImageFile());
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
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = new UploadFile(newProfileImageUrl);
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .newPassword(newPassword)
                .originalPassword(PASSWORD)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(newPassword, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(newNickname);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_카카오Account_모든필드업데이트_성공() throws Exception {
        // Given
        KakaoAccount kakaoAccount = new KakaoAccount(123L, ROLES, PROFILE_IMAGE_FILE);
        kakaoAccount.updateNickname(NICKNAME);
        kakaoAccountRepository.save(kakaoAccount);

        String newNickname = "newNickname";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = new UploadFile(newProfileImageUrl);
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .build();

        // When
        accountService.updateProfile(kakaoAccount, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(kakaoAccount.getUsername()).isNull();
        AssertionsForClassTypes.assertThat(kakaoAccount.getPassword()).isNull();
        AssertionsForClassTypes.assertThat(kakaoAccount.getNickname()).isEqualTo(newNickname);
        AssertionsForClassTypes.assertThat(kakaoAccount.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_모든필드NULL_업데이트안함() throws Exception {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(null)
                .newPassword(null)
                .originalPassword(PASSWORD)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_OriginalPassword없음_오류발생() throws Exception {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = new UploadFile(newProfileImageUrl);
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .newPassword(newPassword)
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
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = new UploadFile(newProfileImageUrl);
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .newPassword(newPassword)
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
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = new UploadFile(newProfileImageUrl);
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .newPassword(newPassword)
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
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = new UploadFile(newProfileImageUrl);
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(NICKNAME)
                .newPassword(newPassword)
                .originalPassword(PASSWORD)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(newPassword, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_같은nickname존재_오류발생() throws Exception {
        // Given
        String account1Username = "account1Username";
        String account1Nickname = "account1Nickname";
        Account account1 = accountService.save(new Account(account1Username, PASSWORD, account1Nickname, PROFILE_IMAGE_FILE, ROLES));

        String account2Username = "account2Username";
        String account2Nickname = "account2Nickname";
        Account account2 = accountService.save(new Account(account2Username, PASSWORD, account2Nickname, PROFILE_IMAGE_FILE, ROLES));

        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = new UploadFile(newProfileImageUrl);
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(account1Nickname)
                .newPassword(newPassword)
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
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        // When
        accountService.delete(account);

        // Then
        // delete후 find하면 없어야 정상
        assertThatThrownBy(() -> accountService.findById(account.getId()))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void delete_KakaoAccount삭제_성공() throws Exception {
        // Given
        Mockito.doNothing().when(kakaoWebService).disconnect(any());
        Account account = accountRepository.save(new KakaoAccount(123L, Set.of(Role.USER), PROFILE_IMAGE_FILE));

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
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        // When
        String newNickname = "newNickname";
        accountService.updateNickname(account, newNickname);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(newNickname);
        AssertionsForClassTypes.assertThat(account.getProfileImageFile()).isEqualTo(PROFILE_IMAGE_FILE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_존재하지않는AccountId_오류발생() {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));
        account.updateId(12345L);

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateNickname(account, NICKNAME));
    }

    @Test
    void updateNickname_같은Nickname으로변경_변경없음() {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        // When
        accountService.updateNickname(account, NICKNAME);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImageFile()).isEqualTo(PROFILE_IMAGE_FILE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_NULL로변경_변경없음() {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        // When
        accountService.updateNickname(account, null);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImageFile()).isEqualTo(PROFILE_IMAGE_FILE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_이미존재하는Nickname으로변경_오류발생() {
        // Given
        String account1Username = "account1Username";
        String account1Nickname = "account1Nickname";
        Account account1 = accountService.save(new Account(account1Username, PASSWORD, account1Nickname, PROFILE_IMAGE_FILE, ROLES));

        String account2Username = "account2Username";
        String account2Nickname = "account2Nickname";
        Account account2 = accountService.save(new Account(account2Username, PASSWORD, account2Nickname, PROFILE_IMAGE_FILE, ROLES));

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateNickname(account2, account1Nickname))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

}