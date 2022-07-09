package com.projectboated.backend.domain.account.account.service;

import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.entity.KakaoAccount;
import com.projectboated.backend.domain.account.account.entity.Role;
import com.projectboated.backend.common.basetest.BaseTest;
import com.projectboated.backend.domain.account.account.service.AccountNicknameService;
import com.projectboated.backend.domain.account.account.service.AccountService;
import com.projectboated.backend.domain.account.account.service.exception.AccountNicknameAlreadyExistsException;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.account.account.service.exception.AccountPasswordWrong;
import com.projectboated.backend.domain.account.account.service.exception.AccountUsernameAlreadyExistsException;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.repository.KakaoAccountRepository;
import com.projectboated.backend.domain.account.account.service.condition.AccountUpdateCond;
import com.projectboated.backend.domain.account.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.domain.account.profileimage.service.AwsS3ProfileImageService;
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

import static com.projectboated.backend.common.data.BasicAccountData.*;
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
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImage()).isEqualTo(URL_PROFILE_IMAGE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void save_중복된Username저장_오류발생() throws Exception {
        // Given
        accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

        // When
        // Then
        String newNickname = "newNickname";
        assertThatThrownBy(() -> accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, newNickname, URL_PROFILE_IMAGE, ROLES)))
                .isInstanceOf(AccountUsernameAlreadyExistsException.class);
    }

    @Test
    void save_중복된Nickname저장_오류발생() throws Exception {
        // Given
        accountRepository.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

        // When
        // Then
        String newUsername = "newUsername";
        assertThatThrownBy(() -> accountService.save(new Account(ACCOUNT_ID, newUsername, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES)))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

    @Test
    void findById_존재하는Id로조회_조회성공() {
        // Given
        Account account = new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES);
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
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
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
        KakaoAccount kakaoAccount = new KakaoAccount(123L, ROLES, URL_PROFILE_IMAGE);
        kakaoAccount.changeNickname(NICKNAME);
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
        AssertionsForClassTypes.assertThat(kakaoAccount.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_모든필드NULL_업데이트안함() throws Exception {
        // Given
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));
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
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

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
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

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
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .nickname(newNickname)
                .newPassword(newPassword)
                .originalPassword(PASSWORD)
                .build();

        // When
//        account.changeId(123123L);

        // Then
        assertThatThrownBy(() -> accountService.updateProfile(account, updateCondition))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void updateProfile_같은nickname으로업데이트_성공() throws Exception {
        // Given
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
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
        Account account1 = accountService.save(new Account(ACCOUNT_ID, account1Username, PASSWORD, account1Nickname, URL_PROFILE_IMAGE, ROLES));

        String account2Username = "account2Username";
        String account2Nickname = "account2Nickname";
        Account account2 = accountService.save(new Account(ACCOUNT_ID, account2Username, PASSWORD, account2Nickname, URL_PROFILE_IMAGE, ROLES));

        String newPassword = "newPassword";
        String newProfileImageUrl = "newProfileImageUrl";
        UploadFile newProfileImageFile = UPLOADFILE_INSTANCE;
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
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

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
        Account account = accountRepository.save(new KakaoAccount(123L, Set.of(Role.USER), URL_PROFILE_IMAGE));

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
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

        // When
        String newNickname = "newNickname";
        accountNicknameService.updateNickname(account, newNickname);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(newNickname);
        AssertionsForClassTypes.assertThat(account.getProfileImage()).isEqualTo(URL_PROFILE_IMAGE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_존재하지않는AccountId_오류발생() {
        // Given
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));
//        account.changeId(12345L);

        // When
        // Then
        assertThatThrownBy(() -> accountNicknameService.updateNickname(account, NICKNAME));
    }

    @Test
    void updateNickname_같은Nickname으로변경_변경없음() {
        // Given
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

        // When
        accountNicknameService.updateNickname(account, NICKNAME);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImage()).isEqualTo(URL_PROFILE_IMAGE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_NULL로변경_변경없음() {
        // Given
        Account account = accountService.save(new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES));

        // When
        accountNicknameService.updateNickname(account, null);

        // Then
        AssertionsForClassTypes.assertThat(account.getUsername()).isEqualTo(USERNAME);
        AssertionsForClassTypes.assertThat(passwordEncoder.matches(PASSWORD, account.getPassword())).isTrue();
        AssertionsForClassTypes.assertThat(account.getNickname()).isEqualTo(NICKNAME);
        AssertionsForClassTypes.assertThat(account.getProfileImage()).isEqualTo(URL_PROFILE_IMAGE);
        AssertionsForClassTypes.assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_이미존재하는Nickname으로변경_오류발생() {
        // Given
        String account1Username = "account1Username";
        String account1Nickname = "account1Nickname";
        Account account1 = accountService.save(new Account(ACCOUNT_ID, account1Username, PASSWORD, account1Nickname, URL_PROFILE_IMAGE, ROLES));

        String account2Username = "account2Username";
        String account2Nickname = "account2Nickname";
        Account account2 = accountService.save(new Account(ACCOUNT_ID, account2Username, PASSWORD, account2Nickname, URL_PROFILE_IMAGE, ROLES));

        // When
        // Then
        assertThatThrownBy(() -> accountNicknameService.updateNickname(account2, account1Nickname))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

}