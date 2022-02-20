package my.sleepydeveloper.projectcompass.domain.account.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private String username = "username";
    private String password = "password";
    private String nickname = "nickname";
    private String role = "ROLE_USER";

    @Test
    @DisplayName("nickname, password 둘다 바꾸기")
    void updateProfile_change_nickname_password() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        // When
        String updatePassword = "updatePassword";
        String updateNickname = "updateNickname";
        account.updateProfile(updateNickname, updatePassword);

        // Then
        assertThat(account.getPassword()).isEqualTo(updatePassword);
        assertThat(account.getNickname()).isEqualTo(updateNickname);
    }

    @Test
    @DisplayName("아무것도 안 바꾸기")
    void updateProfile_dont_change_nickname_password() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, role);

        // When
        account.updateProfile(null, null);

        // Then
        assertThat(account.getPassword()).isEqualTo(password);
        assertThat(account.getNickname()).isEqualTo(nickname);
    }

}