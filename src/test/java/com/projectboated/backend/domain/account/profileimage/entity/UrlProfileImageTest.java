package com.projectboated.backend.domain.account.profileimage.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UrlProfileImageTest {
    
    @Test
    void 생성자_UrlProfileImage생성_return_생성된UrlProfileImage() {
        // Given
        // When
        String url = "url";
        UrlProfileImage urlProfileImage = new UrlProfileImage(url);
        
        // Then
        assertThat(urlProfileImage.getUrl()).isEqualTo(url);
    }

}