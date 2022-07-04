package com.projectboated.backend.domain.account.entity;

import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.common.data.BasicUploadFileData;
import com.projectboated.backend.common.basetest.BaseTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccountTest extends BaseTest {

    @Test
    void constructor_Account생성_정상() {
        // Given
        // When
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);

        // Then
        assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        assertThat(account.getPassword()).isEqualTo(BasicAccountData.PASSWORD);
        assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateNickname_닉네임업데이트_성공() throws Exception {
        // Given
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);

        // When
        String newNickname = "newNickname";
        account.changeNickname(newNickname);

        // Then
        assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        assertThat(account.getPassword()).isEqualTo(BasicAccountData.PASSWORD);
        assertThat(account.getNickname()).isEqualTo(newNickname);
        assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateNickname_NULL로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);

        // When
        account.changeNickname(null);

        // Then
        assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        assertThat(account.getPassword()).isEqualTo(BasicAccountData.PASSWORD);
        assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updatePassword_패스워드업데이트_성공() throws Exception {
        // Given
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);

        // When
        String newPassword = "newPassword";
        account.changePassword(newPassword);

        // Then
        assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        assertThat(account.getPassword()).isEqualTo(newPassword);
        assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updatePassword_NULL로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);

        // When
        account.changePassword(null);

        // Then
        assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        assertThat(account.getPassword()).isEqualTo(BasicAccountData.PASSWORD);
        assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
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
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);

        // When
        Long newId = 123L;
        account.changeId(newId);

        // Then
        assertThat(account.getId()).isEqualTo(newId);
        assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        assertThat(account.getPassword()).isEqualTo(BasicAccountData.PASSWORD);
        assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateId_NULL로업데이트_업데이트안함() throws Exception {
        // Given
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);

        // When
        account.changeId(null);

        // Then
        assertThat(account.getId()).isEqualTo(null);
        assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        assertThat(account.getPassword()).isEqualTo(BasicAccountData.PASSWORD);
        assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateProfile_모든정보업데이트_업데이트성공() throws Exception {
        // Given
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);

        // When
        String updatePassword = "updatePassword";
        String updateNickname = "updateNickname";
        String updateProfileUrl = "updateProfileUrl";
        UploadFile updateProfileFile = BasicUploadFileData.UPLOADFILE_INSTANCE;
        account.changeProfile(updateNickname, updatePassword);

        // Then
        assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        assertThat(account.getPassword()).isEqualTo(updatePassword);
        assertThat(account.getNickname()).isEqualTo(updateNickname);
        assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void updateProfile_모든정보NULL_기존정보유지() throws Exception {
        // Given
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);

        // When
        account.changeProfile(null, null);

        // Then
        assertThat(account.getUsername()).isEqualTo(BasicAccountData.USERNAME);
        assertThat(account.getPassword()).isEqualTo(BasicAccountData.PASSWORD);
        assertThat(account.getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        assertThat(account.getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

}