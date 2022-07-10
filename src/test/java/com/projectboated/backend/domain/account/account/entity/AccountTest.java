package com.projectboated.backend.domain.account.account.entity;

import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.account.profileimage.entity.UrlProfileImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Account : Entity 단위 테스트")
class AccountTest {

    @Test
    void 생성자_account생성_return_생성된Account() {
        // Given
        // When
        Account account = new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(URL_PROFILE_IMAGE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void changeNickname_null이아닌Nickname_닉네임변경() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();

        // When
        String newNickname = "newNickname";
        account.changeNickname(newNickname);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(newNickname);
        assertThat(account.getProfileImage()).isEqualTo(URL_PROFILE_IMAGE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void changeNickname_nickname이null_닉네임변경안함() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();

        // When
        account.changeNickname(null);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(URL_PROFILE_IMAGE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void changePassword_null이아닌Password_Password변경() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();

        // When
        String newPassword = "newPassword";
        account.changePassword(newPassword);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(newPassword);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(URL_PROFILE_IMAGE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void changePassword_nickname이null_Password변경안함() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();

        // When
        account.changePassword(null);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(URL_PROFILE_IMAGE);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

    @Test
    void changeProfileImage_새로운profileImage_ProfileUrl업데이트() {
        // Given
        Account account = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();

        // When
        String newProfileImageUrl = "newProfileImageUrl";
        ProfileImage newProfileImage = new UrlProfileImage(newProfileImageUrl);
        account.changeProfileImage(newProfileImage);

        // Then
        assertThat(account.getUsername()).isEqualTo(USERNAME);
        assertThat(account.getPassword()).isEqualTo(PASSWORD);
        assertThat(account.getNickname()).isEqualTo(NICKNAME);
        assertThat(account.getProfileImage()).isEqualTo(newProfileImage);
        assertThat(account.getRoles()).isEqualTo(ROLES);
    }

}