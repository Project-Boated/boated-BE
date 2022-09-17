package com.projectboated.backend.account.account.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataAccount.ROLES;
import static com.projectboated.backend.utils.data.BasicDataAccount.URL_PROFILE_IMAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("KakaoAccount : Entity 단위 테스트")
class KakaoAccountTest {

    @Test
    void 생성자_kakaoAccount생성_return_생성된kakaoAccount() {
        // Given
        // When
        long kakaoId = 123L;
        KakaoAccount kakaoAccount = new KakaoAccount(kakaoId, ROLES, URL_PROFILE_IMAGE);

        // Then
        assertThat(kakaoAccount.getUsername()).isEqualTo(null);
        assertThat(kakaoAccount.getPassword()).isEqualTo(null);
        assertThat(kakaoAccount.getNickname()).isEqualTo(null);
        assertThat(kakaoAccount.getKakaoId()).isEqualTo(kakaoId);
        assertThat(kakaoAccount.getRoles()).isEqualTo(ROLES);
        assertThat(kakaoAccount.getProfileImage()).isEqualTo(URL_PROFILE_IMAGE);
    }

}