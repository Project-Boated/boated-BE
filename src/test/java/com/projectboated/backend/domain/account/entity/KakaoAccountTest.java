package com.projectboated.backend.domain.account.entity;

import com.projectboated.backend.common.data.BasicAccountData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class KakaoAccountTest {

    @Test
    void constructor_하나생성_성공() {
        // given, when
        long kakaoId = 123L;
        KakaoAccount kakaoAccount = new KakaoAccount(kakaoId, BasicAccountData.ROLES, BasicAccountData.PROFILE_IMAGE_FILE);

        // then
        assertThat(kakaoAccount.getKakaoId()).isEqualTo(kakaoId);
        assertThat(kakaoAccount.getRoles()).isEqualTo(BasicAccountData.ROLES);
        assertThat(kakaoAccount.getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
    }

}