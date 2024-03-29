package com.projectboated.backend.account.profileimage.controller;

import com.projectboated.backend.account.profileimage.controller.document.ProfileImageDocument;
import com.projectboated.backend.utils.base.ControllerTest;
import com.projectboated.backend.infra.aws.AwsS3File;
import com.projectboated.backend.utils.controller.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProfileImage : Controller 단위테스트")
class ProfileImageControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void postAccountProfileImage_프로필이미지수정_성공() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "image/jpg", "file".getBytes());

        // When
        // Then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/account/profile/profile-image")
                        .file(file))
                .andExpect(status().isOk())
                .andDo(ProfileImageDocument.documentAccountProfileImageUpdate());

        verify(profileImageService).updateProfileImage(any(), any());
    }

    @Test
    @WithMockAccount
    void getAccountProfileImage_이미지Get_성공() throws Exception {
        // Given
        when(awsS3ProfileImageService.getProfileImageBytes(any())).thenReturn(new AwsS3File("filename", "image/png", "asdf".getBytes()));

        // When
        // Then
        mockMvc.perform(get("/api/account/profile/profile-image"))
                .andExpect(status().isOk())
                .andDo(ProfileImageDocument.documentAccountProfileImageRetrieve());
    }

    @Test
    @WithMockAccount
    void deleteAccountProfileImage_이미지삭제_성공() throws Exception {
        // Given
        // When
        // Then
        mockMvc.perform(delete("/api/account/profile/profile-image"))
                .andExpect(status().isOk())
                .andDo(ProfileImageDocument.documentAccountProfileImageDelete());

        verify(profileImageService).deleteProfileImageFile(any());
    }

}