package my.sleepydeveloper.projectcompass.domain.account.entity;

import my.sleepydeveloper.projectcompass.common.basetest.UnitTest;
import org.junit.jupiter.api.Test;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccountTest extends UnitTest {

    @Test
    void constructor_Account생성_정상() {
        // Given
        // When
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_닉네임업데이트_성공() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        String newNickname = "newNickname";
        account.updateNickname(newNickname);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(newNickname);
        assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateNickname_NULL로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        account.updateNickname(null);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updatePassword_패스워드업데이트_성공() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        String newPassword = "newPassword";
        account.updatePassword(newPassword);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(newPassword);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updatePassword_NULL로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        account.updatePassword(null);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfileUrl_ProfileUrl업데이트_성공() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        String newProfileUrl = "newProfileUrl";
        account.updateProfileImageUrl(newProfileUrl);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImageUrl()).isEqualTo(newProfileUrl);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfileUrl_NULL로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        account.updateProfileImageUrl(null);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateId_Id업데이트_성공() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        Long newId = 123L;
        account.updateId(newId);

        // Then
        assertThat(account.getId()).isEqualTo(newId);
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateId_NULL로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        account.updateId(null);

        // Then
        assertThat(account.getId()).isEqualTo(null);
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_모든정보업데이트_업데이트성공() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        String updatePassword = "updatePassword";
        String updateNickname = "updateNickname";
        String updateProfileUrl = "updateProfileUrl";
        account.updateProfile(updateNickname, updatePassword, updateProfileUrl);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(updatePassword);
        assertThat(account.getNickname()).isEqualTo(updateNickname);
        assertThat(account.getProfileImageUrl()).isEqualTo(updateProfileUrl);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void updateProfile_모든정보NULL_기존정보유지() throws Exception {
        // Given
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        account.updateProfile(null, null, null);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

}