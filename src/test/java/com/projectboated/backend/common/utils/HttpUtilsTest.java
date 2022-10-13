package com.projectboated.backend.common.utils;

import com.projectboated.backend.common.utils.exception.MultiPartFileIsEmptyException;
import com.projectboated.backend.common.utils.exception.NotImageFileException;
import com.projectboated.backend.account.profileimage.entity.exception.ProfileImageNeedsHostUrlException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import static com.projectboated.backend.utils.data.BasicDataUploadFile.ORIGINAL_FILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("HttpUtils : Utils 단위테스트")
class HttpUtilsTest {

    @Test
    void validateIsImageFile_multipartFile이null일경우_return() {
        // Given
        HttpUtils httpUtils = new HttpUtils();

        // When
        // Then
        httpUtils.validateIsImageFile(null);
    }

    @Test
    void validateIsImageFile_multipartFile이empty일경우_리턴() {
        // Given
        HttpUtils httpUtils = new HttpUtils();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", (byte[]) null);

        // When
        // Then
        httpUtils.validateIsImageFile(mockMultipartFile);
    }

    @Test
    void validateIsImageFile_mediaType이null인경우_예외발생() {
        // Given
        HttpUtils httpUtils = new HttpUtils();

        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, null, "content".getBytes());

        // When
        // Then
        assertThatThrownBy(() -> httpUtils.validateIsImageFile(multipartFile))
                .isInstanceOf(NotImageFileException.class);
    }

    @Test
    void validateIsImageFile_mediaType이image가아닌경우_예외발생() {
        // Given
        HttpUtils httpUtils = new HttpUtils();

        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MediaType.APPLICATION_JSON_VALUE, "content".getBytes());

        // When
        // Then
        assertThatThrownBy(() -> httpUtils.validateIsImageFile(multipartFile))
                .isInstanceOf(NotImageFileException.class);
    }

    @Test
    void validateIsImageFile_mediaType이image일경우_정상리턴() {
        // Given
        HttpUtils httpUtils = new HttpUtils();

        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

        // When
        // Then
        httpUtils.validateIsImageFile(multipartFile);
    }
    
    @Test
    void getHostUrl_proxy요청일경우_return_localhost(){
        // Given
        HttpUtils httpUtils = new HttpUtils();
        
        // When
        String hostUrl = httpUtils.getHostUrl(null, true);

        // Then
        assertThat(hostUrl).isEqualTo("localhost:3000");
    }

    @Test
    void getHostUrl_hostHeader가null일경우_예외발생(){
        // Given
        HttpUtils httpUtils = new HttpUtils();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // When
        // Then
        assertThatThrownBy(() -> httpUtils.getHostUrl(httpServletRequest, false))
                .isInstanceOf(ProfileImageNeedsHostUrlException.class);
    }

    @Test
    void getHostUrl_proxy가아니고hostHeader가존재할경우_return_host(){
        // Given
        HttpUtils httpUtils = new HttpUtils();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        String hosturl = "hosturl";
        httpServletRequest.addHeader(HttpHeaders.HOST, hosturl);

        // When
        String result = httpUtils.getHostUrl(httpServletRequest, false);

        // Then
        assertThat(result).isEqualTo(hosturl);
    }

}