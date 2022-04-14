package my.sleepydeveloper.projectcompass.domain.account.entity;

import my.sleepydeveloper.projectcompass.common.basetest.UnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccountTest extends UnitTest {

    @Test
    void updateProfile_모든정보업데이트_업데이트성공() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, userRole);

        // When
        String updatePassword = "updatePassword";
        String updateNickname = "updateNickname";
        String updateProfileUrl = "updateProfileUrl";
        account.updateProfile(updateNickname, updatePassword, updateProfileUrl);

        // Then
        assertThat(account.getPassword()).isEqualTo(updatePassword);
        assertThat(account.getNickname()).isEqualTo(updateNickname);
        assertThat(account.getProfileUrl()).isEqualTo(updateProfileUrl);
    }

    @Test
    void updateProfile_모든정보NULL_기존정보유지() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, userRole);

        // When
        account.updateProfile(null, null, null);

        // Then
        assertThat(account.getPassword()).isEqualTo(password);
        assertThat(account.getNickname()).isEqualTo(nickname);
    }

}