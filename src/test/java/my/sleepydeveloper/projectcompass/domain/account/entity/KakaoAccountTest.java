package my.sleepydeveloper.projectcompass.domain.account.entity;

import org.junit.jupiter.api.Test;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class KakaoAccountTest {

    @Test
    void constructor_하나생성_성공() {
        // given, when
        long kakaoId = 123L;
        KakaoAccount kakaoAccount = new KakaoAccount(kakaoId, ROLES, PROFILE_IMAGE_FILE);

        // then
        assertThat(kakaoAccount.getKakaoId()).isEqualTo(kakaoId);
        assertThat(kakaoAccount.getRoles()).isEqualTo(ROLES);
        assertThat(kakaoAccount.getProfileImage()).isEqualTo(PROFILE_IMAGE_FILE);
    }

}