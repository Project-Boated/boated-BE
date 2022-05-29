package my.sleepydeveloper.projectcompass.domain.account.entity;

import my.sleepydeveloper.projectcompass.common.basetest.BaseTest;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.ProfileImage;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicUploadFileData.UPLOADFILE_INSTANCE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccountTest extends BaseTest {

    @Test
    void constructor_Account생성_정상() {
        // Given
        // When
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_닉네임업데이트_성공() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);

        // When
        String newNickname = "newNickname";
        account.updateNickname(newNickname);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(newNickname);
        assertThat(account.getProfileImage()).isEqualTo(PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_NULL로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);

        // When
        account.updateNickname(null);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updatePassword_패스워드업데이트_성공() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);

        // When
        String newPassword = "newPassword";
        account.updatePassword(newPassword);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(newPassword);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updatePassword_NULL로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);

        // When
        account.updatePassword(null);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    @Disabled
    void updateProfileUrl_ProfileUrl업데이트_성공() throws Exception {
//        // Given
//        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);
//
//        // When
//        String newProfileUrl = "newProfileUrl";
//        ProfileImage newProfileImageFile = new UploadFile(newProfileUrl);
//        account.updateProfileImage(newProfileImageFile);
//
//        // Then
//        assertThat(account.getUsername()).isEqualTo(USERNAME);
//        assertThat(account.getPassword()).isEqualTo(PASSWORD);
//        assertThat(account.getNickname()).isEqualTo(NICKNAME);
//        assertThat(account.getProfileImage()).isEqualTo(newProfileImageFile);
//        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateId_Id업데이트_성공() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);

        // When
        Long newId = 123L;
        account.updateId(newId);

        // Then
        assertThat(account.getId()).isEqualTo(newId);
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateId_NULL로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);

        // When
        account.updateId(null);

        // Then
        assertThat(account.getId()).isEqualTo(null);
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_모든정보업데이트_업데이트성공() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);

        // When
        String updatePassword = "updatePassword";
        String updateNickname = "updateNickname";
        String updateProfileUrl = "updateProfileUrl";
        UploadFile updateProfileFile = UPLOADFILE_INSTANCE;
        account.updateProfile(updateNickname, updatePassword);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(updatePassword);
        assertThat(account.getNickname()).isEqualTo(updateNickname);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_모든정보NULL_기존정보유지() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);

        // When
        account.updateProfile(null, null);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

}