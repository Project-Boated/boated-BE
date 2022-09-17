package com.projectboated.backend.account.account.service;

import com.google.common.net.MediaType;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.entity.KakaoAccount;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.condition.AccountUpdateCond;
import com.projectboated.backend.account.account.service.exception.AccountNicknameAlreadyExistsException;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.account.account.service.exception.AccountPasswordWrong;
import com.projectboated.backend.account.account.service.exception.AccountUsernameAlreadyExistsException;
import com.projectboated.backend.account.profileimage.entity.UploadFileProfileImage;
import com.projectboated.backend.account.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.infra.aws.AwsS3ProfileImageService;
import com.projectboated.backend.security.service.KakaoWebService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Account : Service 단위 테스트")
class AccountServiceTest extends ServiceTest {

    @InjectMocks
    AccountService accountService;

    @Mock
    AwsS3ProfileImageService awsS3ProfileImageService;
    @Mock
    KakaoWebService kakaoWebService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    ProfileImageRepository profileImageRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void findById_존재하는Id_return_Account1개() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(ACCOUNT));

        // When
        Account result = accountService.findById(1L);

        // Then
        assertThat(result).isEqualTo(ACCOUNT);
    }

    @Test
    void findById_존재하지않는Id_예외발생() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> accountService.findById(1L))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void save_존재하지않는username존재하지않는nickname_return_Account() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(accountRepository.existsByNickname(NICKNAME)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);

        // When
        Account saveAccount = accountService.save(account);

        // Then
        assertThat(saveAccount).isEqualTo(account);
    }

    @Test
    void save_username이null인경우_assertionError() {
        // Given
        Account account = Account.builder()
                .username(null)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        // When
        // Then
        assertThatThrownBy(() -> accountService.save(account))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    void save_nickname이null인경우_assertionError() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(null)
                .password(PASSWORD)
                .build();

        // When
        // Then
        assertThatThrownBy(() -> accountService.save(account))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    void save_password가null인경우_assertionError() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(null)
                .build();

        // When
        // Then
        assertThatThrownBy(() -> accountService.save(account))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    void save_존재하는username_예외발생() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.existsByUsername(USERNAME)).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> accountService.save(account))
                .isInstanceOf(AccountUsernameAlreadyExistsException.class);
    }

    @Test
    void save_존재하는nickname_예외발생() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(accountRepository.existsByNickname(NICKNAME)).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> accountService.save(account))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

    @Test
    void updateProfile_accountById찾을수없음_예외발생() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        AccountUpdateCond cond = AccountUpdateCond.builder()
                .build();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateProfile(account.getId(), cond))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void updateProfile_originalPassword틀림_예외발생() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        AccountUpdateCond cond = AccountUpdateCond.builder()
                .originalPassword("fail")
                .build();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateProfile(account.getId(), cond))
                .isInstanceOf(AccountPasswordWrong.class);
    }

    @Test
    void updateProfile_이미존재하는Nickname_예외발생() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        String newNickname = "updateNickname";
        AccountUpdateCond cond = AccountUpdateCond.builder()
                .nickname(newNickname)
                .originalPassword(PASSWORD)
                .build();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(accountRepository.existsByNickname(cond.getNickname())).thenReturn(true);
        when(passwordEncoder.matches(cond.getOriginalPassword(), PASSWORD)).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> accountService.updateProfile(account.getId(), cond))
                .isInstanceOf(AccountNicknameAlreadyExistsException.class);
    }

    @Test
    void updateProfile_nickname만변경_변경성공() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        String newNickname = "newNickname";
        AccountUpdateCond cond = AccountUpdateCond.builder()
                .originalPassword(PASSWORD)
                .nickname(newNickname)
                .build();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(accountRepository.existsByNickname(cond.getNickname())).thenReturn(false);
        when(passwordEncoder.matches(cond.getOriginalPassword(), PASSWORD)).thenReturn(true);

        // When
        accountService.updateProfile(account.getId(), cond);

        // Then
        assertThat(account.getNickname()).isEqualTo(newNickname);
    }

    @Test
    void updateProfile_password만변경_변경성공() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        String newPassword = "newPassword";
        AccountUpdateCond cond = AccountUpdateCond.builder()
                .originalPassword(PASSWORD)
                .newPassword(newPassword)
                .build();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(cond.getOriginalPassword(), PASSWORD)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);

        // When
        accountService.updateProfile(account.getId(), cond);

        // Then
        assertThat(account.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void updateProfile_profileImage만변경_변경성공() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        MockMultipartFile newMultipartFile = new MockMultipartFile("file",
                "file.txt",
                MediaType.JPEG.toString(),
                "content".getBytes());

        AccountUpdateCond cond = AccountUpdateCond.builder()
                .originalPassword(PASSWORD)
                .profileImageFile(newMultipartFile)
                .build();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(cond.getOriginalPassword(), PASSWORD)).thenReturn(true);
        when(awsS3ProfileImageService.uploadProfileImage(any(), any(), any())).thenReturn(null);
        when(profileImageRepository.save(any())).thenReturn(null);

        // When
        accountService.updateProfile(account.getId(), cond);

        // Then
        UploadFile uploadFile = ((UploadFileProfileImage) account.getProfileImage()).getUploadFile();
        assertThat(uploadFile.getOriginalFileName()).isEqualTo("file");
        assertThat(uploadFile.getExt()).isEqualTo("txt");
    }

    @Test
    void updateProfile_전체변경_예외발생() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        MockMultipartFile newMultipartFile = new MockMultipartFile("file",
                "file.txt",
                MediaType.JPEG.toString(),
                "content".getBytes());

        String newPassword = "newPassword";
        String newNickname = "newNickname";

        AccountUpdateCond cond = AccountUpdateCond.builder()
                .originalPassword(PASSWORD)
                .newPassword(newPassword)
                .nickname(newNickname)
                .profileImageFile(newMultipartFile)
                .build();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(accountRepository.existsByNickname(cond.getNickname())).thenReturn(false);
        when(passwordEncoder.matches(cond.getOriginalPassword(), PASSWORD)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        when(awsS3ProfileImageService.uploadProfileImage(any(), any(), any())).thenReturn(null);
        when(profileImageRepository.save(any())).thenReturn(null);

        // When
        accountService.updateProfile(account.getId(), cond);

        // Then
        UploadFile uploadFile = ((UploadFileProfileImage) account.getProfileImage()).getUploadFile();
        assertThat(uploadFile.getOriginalFileName()).isEqualTo("file");
        assertThat(uploadFile.getExt()).isEqualTo("txt");
        assertThat(account.getNickname()).isEqualTo(newNickname);
        assertThat(account.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void delete_account주어짐_삭제성공() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // When
        // Then
        accountService.delete(ACCOUNT_ID);
    }

    @Test
    void delete_accountId으로account찾을수없음_예외발생() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> accountService.delete(ACCOUNT_ID))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void delete_카카오account삭제_정상삭제() {
        // Given
        KakaoAccount account = KakaoAccount.kakaoBuilder()
                .kakaoId(ACCOUNT_ID)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // When
        accountService.delete(ACCOUNT_ID);

        // Then
        verify(kakaoWebService).disconnect(ACCOUNT_ID);
    }
}