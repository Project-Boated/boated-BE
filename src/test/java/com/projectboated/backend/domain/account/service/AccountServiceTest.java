package com.projectboated.backend.domain.account.service;

import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.domain.account.entity.KakaoAccount;
import com.projectboated.backend.domain.account.entity.Role;
import com.projectboated.backend.domain.profileimage.service.AwsS3ProfileImageService;
import com.projectboated.backend.common.basetest.BaseTest;
import com.projectboated.backend.domain.account.exception.AccountNicknameAlreadyExistsException;
import com.projectboated.backend.domain.account.exception.AccountNotFoundException;
import com.projectboated.backend.domain.account.exception.AccountPasswordWrong;
import com.projectboated.backend.domain.account.exception.AccountUsernameAlreadyExistsException;
import com.projectboated.backend.domain.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.repository.KakaoAccountRepository;
import com.projectboated.backend.domain.account.service.condition.AccountUpdateCond;
import com.projectboated.backend.domain.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.domain.uploadfile.repository.UploadFileRepository;
import com.projectboated.backend.security.service.KakaoWebService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static com.projectboated.backend.common.data.BasicUploadFileData.UPLOADFILE_INSTANCE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.context.annotation.ComponentScan.Filter;

@DataJpaTest(includeFilters = {
        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = PasswordEncoder.class)
})
@ExtendWith(MockitoExtension.class)
@Disabled
class AccountServiceTest extends BaseTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KakaoAccountRepository kakaoAccountRepository;

    @Autowired
    private ProfileImageRepository profileImageRepository;

    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    KakaoWebService kakaoWebService;

    @Mock
    AwsS3ProfileImageService awsS3ProfileService;

    @Mock
    AccountNicknameService accountNicknameService;

    @Mock
    UploadFileRepository uploadFileRepository;

    @BeforeEach
    void beforeEach() {
        accountService = new AccountService(accountRepository,
                passwordEncoder,
                kakaoWebService,
                awsS3ProfileService,
                accountNicknameService,
                uploadFileRepository,
                profileImageRepository
        );
    }

    @Test
    void save_존재하지않는Username_AND_존재하지않는Nickname_으로저장_성공() throws Exception {
        // Given
        // When
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(BasicAccountData.PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void save_중복된Username저장_오류발생() throws Exception {
        // Given
        accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        // Then
        String newNickname = "newNickname";
        assertThatThrownBy(() -> accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, newNickname, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES)))
                .isInstanceOf(AccountUsernameAlreadyExistsException.class);
    }

    @Test
    void save_중복된Nickname저장_오류발생() throws Exception {
        // Given
        accountRepository.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        // Then
        String newUsername = "newUsername";
        assertThatThrownBy(() -> accountService.save(new Account(newUsername, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES)))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

    @Test
    void findById_존재하는Id로조회_조회성공() {
        // Given
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);
        accountService.save(account);

        // When
        Account findAccount = accountService.findById(account.getId());

        // Then
        AssertionsForClassTypes.assertThat(findAccount.getUsername()).isEqualTo(account.getUsername());
        AssertionsForClassTypes.assertThat(findAccount.getPassword()).isEqualTo(account.getPassword());
        AssertionsForClassTypes.assertThat(findAccount.getNickname()).isEqualTo(account.getNickname());
        AssertionsForClassTypes.assertThat(findAccount.getProfileImage()).isEqualTo(account.getProfileImage());
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
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .newPassword(newPassword)
                .originalPassword(BasicAccountData.PASSWORD)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(newPassword, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(newNickname);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateProfile_카카오Account_모든필드업데이트_성공() throws Exception {
        // Given
        KakaoAccount kakaoAccount = new KakaoAccount(123L, BasicAccountData.ROLES, BasicAccountData.PROFILE_IMAGE_FILE);
        kakaoAccount.updateNickname(BasicAccountData.NICKNAME);
        kakaoAccountRepository.save(kakaoAccount);

        String newNickname = "newNickname";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .build();

        // When
        accountService.updateProfile(kakaoAccount, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(kakaoAccount.getUsername()).isNull();
        AssertionsForClassTypes.assertThat(kakaoAccount.getPassword()).isNull();
        AssertionsForClassTypes.assertThat(kakaoAccount.getNickname()).isEqualTo(newNickname);
        AssertionsForClassTypes.assertThat(kakaoAccount.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateProfile_모든필드NULL_업데이트안함() throws Exception {
        // Given
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(null)
                .newPassword(null)
                .originalPassword(BasicAccountData.PASSWORD)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(BasicAccountData.PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateProfile_OriginalPassword없음_오류발생() throws Exception {
        // Given
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
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
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
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
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .newPassword(newPassword)
                .originalPassword(BasicAccountData.PASSWORD)
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
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(BasicAccountData.NICKNAME)
                .newPassword(newPassword)
                .originalPassword(BasicAccountData.PASSWORD)
                .build();

        // When
        accountService.updateProfile(account, updateCondition);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(newPassword, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateProfile_같은nickname존재_오류발생() throws Exception {
        // Given
        String account1Username = "account1Username";
        String account1Nickname = "account1Nickname";
        Account account1 = accountService.save(new Account(account1Username, BasicAccountData.PASSWORD, account1Nickname, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        String account2Username = "account2Username";
        String account2Nickname = "account2Nickname";
        Account account2 = accountService.save(new Account(account2Username, BasicAccountData.PASSWORD, account2Nickname, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(account1Nickname)
                .newPassword(newPassword)
                .originalPassword(BasicAccountData.PASSWORD)
                .build();

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateProfile(account2, updateCondition))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

    @Test
    void delete_Account삭제_성공() throws Exception {
        // Given
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

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
        Account account = accountRepository.save(new KakaoAccount(123L, Set.of(Role.USER), BasicAccountData.PROFILE_IMAGE_FILE));

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
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        String newNickname = "newNickname";
        accountNicknameService.updateNickname(account, newNickname);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(BasicAccountData.PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(newNickname);
        AssertionsForClassTypes.assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateNickname_존재하지않는AccountId_오류발생() {
        // Given
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        account.updateId(12345L);

        // When
        // Then
        assertThatThrownBy(() -> accountNicknameService.updateNickname(account, BasicAccountData.NICKNAME));
    }

    @Test
    void updateNickname_같은Nickname으로변경_변경없음() {
        // Given
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        accountNicknameService.updateNickname(account, BasicAccountData.NICKNAME);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(BasicAccountData.PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateNickname_NULL로변경_변경없음() {
        // Given
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        accountNicknameService.updateNickname(account, null);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(BasicAccountData.PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateNickname_이미존재하는Nickname으로변경_오류발생() {
        // Given
        String account1Username = "account1Username";
        String account1Nickname = "account1Nickname";
        Account account1 = accountService.save(new Account(account1Username, BasicAccountData.PASSWORD, account1Nickname, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        String account2Username = "account2Username";
        String account2Nickname = "account2Nickname";
        Account account2 = accountService.save(new Account(account2Username, BasicAccountData.PASSWORD, account2Nickname, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        // Then
        assertThatThrownBy(() -> accountNicknameService.updateNickname(account2, account1Nickname))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

}