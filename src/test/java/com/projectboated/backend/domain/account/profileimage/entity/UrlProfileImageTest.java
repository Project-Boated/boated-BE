package com.projectboated.backend.domain.account.profileimage.entity;

import com.projectboated.backend.domain.account.profileimage.entity.exception.ProfileImageNeedsHostUrlException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UrlProfileImage : Entity 단위 테스트")
class UrlProfileImageTest {
    
    @Test
    void 생성자_UrlProfileImage생성_return_생성된UrlProfileImage() {
        // Given
        String url = "url";

        // When
        UrlProfileImage urlProfileImage = new UrlProfileImage(url);
        
        // Then
        assertThat(urlProfileImage.getUrl()).isEqualTo(url);
    }

    @Test
    void getUrl_상관없음_return_url() {
        // Given
        String url = "http://url";
        UrlProfileImage urlProfileImage = new UrlProfileImage(url);

        // When
        String result = urlProfileImage.getUrl(null, false);

        // Then
        assertThat(result).startsWith(url);
    }

}