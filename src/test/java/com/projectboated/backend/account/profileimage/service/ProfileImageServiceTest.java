package com.projectboated.backend.account.profileimage.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.account.account.service.exception.AccountProfileImageFileNotExist;
import com.projectboated.backend.account.profileimage.entity.UploadFileProfileImage;
import com.projectboated.backend.account.profileimage.entity.UrlProfileImage;
import com.projectboated.backend.account.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.utils.base.ServiceTest;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import com.projectboated.backend.infra.aws.AwsS3ProfileImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataAccount.NICKNAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("ProfileImage : Service 단위 테스트")
class ProfileImageServiceTest extends ServiceTest {

    @InjectMocks
    ProfileImageService profileImageService;

    @Mock
    ProfileImageRepository profileImageRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    AwsS3ProfileImageService awsS3ProfileImageService;

    @Test
    void save_ProfileImage하나저장_정상() {
        // Given
        String url = "url";
        UrlProfileImage profileImage = new UrlProfileImage(url);

        when(profileImageRepository.save(profileImage)).thenReturn(profileImage);

        // When
        profileImageService.save(profileImage);

        // Then
        verify(profileImageRepository).save(profileImage);
    }

    @Test
    void updateProfileImage_잘못된accountId_예외발생() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .nickname(NICKNAME)
                .build();
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "file.txt",
                MediaType.IMAGE_JPEG_VALUE,
                "filecontent".getBytes());

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> profileImageService.updateProfileImage(ACCOUNT_ID, multipartFile));
    }

    @Test
    void updateProfileImage_존재하는accoutid_정상저장() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .nickname(NICKNAME)
                .build();
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "file.txt",
                MediaType.IMAGE_JPEG_VALUE,
                "filecontent".getBytes());

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(profileImageRepository.save(any())).thenReturn(null);
        when(awsS3ProfileImageService.uploadProfileImage(any(), any(), any())).thenReturn(null);

        // When
        profileImageService.updateProfileImage(ACCOUNT_ID, multipartFile);

        // Then
        verify(accountRepository).findById(ACCOUNT_ID);
        verify(profileImageRepository).save(any());
        verify(awsS3ProfileImageService).uploadProfileImage(any(), any(), any());
    }

    @Test
    void getProfileUrl_잘못된accountId_예외발생() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .nickname(NICKNAME)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> profileImageService.getProfileUrl(account.getId(), "hostUrl", true))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getProfileUrl_account에profileimage가없을경우_return_null() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .nickname(NICKNAME)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // When
        String result = profileImageService.getProfileUrl(account.getId(), "hostUrl", true);

        // Then
        assertThat(result).isEqualTo(null);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void getProfileUrl_정상입력_return_url() {
        // Given
        UrlProfileImage profileImage = new UrlProfileImage("url");

        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .nickname(NICKNAME)
                .profileImageFile(profileImage)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // When
        profileImageService.getProfileUrl(account.getId(), "hostUrl", true);

        // Then
        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void deleteProfileImageFile_accountId가존재하지않을경우_예외발생() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .nickname(NICKNAME)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> profileImageService.deleteProfileImageFile(account))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void deleteProfileImageFile_account에profileImage없음_예외발생() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .nickname(NICKNAME)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // When
        // Then
        assertThatThrownBy(() -> profileImageService.deleteProfileImageFile(account))
                .isInstanceOf(AccountProfileImageFileNotExist.class);
    }

    @Test
    void deleteProfileImageFile_account에UrlProfileImage있음_profileImage삭제() {
        // Given
        UrlProfileImage profileImage = new UrlProfileImage("url");
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .nickname(NICKNAME)
                .profileImageFile(profileImage)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // When
        profileImageService.deleteProfileImageFile(account);

        // Then
        assertThat(account.getProfileImage()).isNull();

        verify(awsS3ProfileImageService, times(0)).deleteProfileImage(any(), any());
    }

    @Test
    void deleteProfileImageFile_account에UploadFileProfileImage있음_profileImage삭제() {
        // Given
        UploadFile uploadFile = UploadFile.builder()
                .originalFileName("original.txt")
                .saveFileName("saved")
                .mediaType(MediaType.IMAGE_JPEG_VALUE)
                .build();
        UploadFileProfileImage profileImage = new UploadFileProfileImage(uploadFile);
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .nickname(NICKNAME)
                .profileImageFile(profileImage)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // When
        profileImageService.deleteProfileImageFile(account);

        // Then
        assertThat(account.getProfileImage()).isNull();

        verify(awsS3ProfileImageService, times(1)).deleteProfileImage(any(), any());
    }

}