package com.projectboated.backend.web.account.account.controller;

import com.google.common.net.MediaType;
import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.web.account.account.dto.request.SignUpRequest;
import com.projectboated.backend.web.config.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.web.account.account.controller.document.AccountDocument.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Account : Controller 단위테스트")
class AccountControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void signUp_회원가입_성공() throws Exception {
        // Given
        SignUpRequest requestBody = SignUpRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();

        // When
        // Then
        mockMvc.perform(post("/api/account/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(requestBody)))
                .andExpect(status().isOk())
                .andDo(documentSignUp());

        verify(accountService).save(any());
    }

    @Test
    @WithMockAccount
    void getAccountProfile_자신의profile가져오기_성공() throws Exception {
        // Given
        when(accountService.findById(anyLong())).thenReturn(createDefaultAccount());
        when(profileImageService.getProfileUrl(any(), any(), anyBoolean())).thenReturn("url");

        // When
        // Then
        mockMvc.perform(get("/api/account/profile"))
                .andExpect(status().isOk())
                .andDo(documentAccountProfileRetrieve());

        verify(accountService).findById(anyLong());
        verify(profileImageService).getProfileUrl(any(), any(), anyBoolean());
    }

    @Test
    @WithMockAccount
    void patchAccountProfile_프로필update_성공() throws Exception {
        // Given
        when(accountService.findById(anyLong())).thenReturn(createDefaultAccount());
        when(profileImageService.getProfileUrl(any(), any(), anyBoolean())).thenReturn("url");

        // When
        // Then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/account/profile")
                        .file(new MockMultipartFile("profileImageFile", "file.txt",
                                MediaType.JPEG.toString(), "profileFileImage".getBytes()))
                        .param("nickname", "newNickname")
                        .param("originalPassword", "newOriginalPassword")
                        .param("newPassword", "newPassword"))
                .andExpect(status().isOk())
                .andDo(documentAccountProfileUpdate());

        verify(accountService).updateProfile(any(), any());
    }

    @Test
    @WithMockAccount
    void deleteAccount_회원탈퇴_성공() throws Exception {
        // Given
        // When
        // Then
        mockMvc.perform(delete("/api/account/profile"))
                .andExpect(status().isOk())
                .andDo(documentAccountDelete());

        verify(accountService).delete(any());
    }



    /*
    @Test
    void postAccountProfileImage_프로필이미지수정_성공() throws IOException {
        Mockito.when(awsS3ProfileService.uploadProfileImage(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn("http://test.com");

        AccountTestUtils.createAccount(port, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD, BasicDataAccount.NICKNAME, BasicDataAccount.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD);

        given(this.spec)
            .filter(documentAccountProfileImageUpdate())
            .accept(ContentType.JSON)
            .contentType(ContentType.MULTIPART)
            .cookie(cookie)
        .when()
            .port(port)
            .multiPart("file", ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg")
            .post("/api/account/profile/profile-image")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getAccountProfileImage_이미지Get_성공() throws IOException {
        AccountTestUtils.createAccount(port, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD, BasicDataAccount.NICKNAME, BasicDataAccount.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD);

        Mockito.when(awsS3ProfileService.getProfileImageBytes(Mockito.any()))
                .thenReturn(new AwsS3File("filename", "image/png", "asdf".getBytes()));

        given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.MULTIPART)
            .cookie(cookie)
        .when()
            .port(port)
            .multiPart("file", ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg")
            .post("/api/account/profile/profile-image");


        given(this.spec)
            .filter(documentAccountProfileImageRetrieve())
            .cookie(cookie)
        .when()
            .port(port)
            .get("/api/account/profile/profile-image")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteAccountProfileImage_이미지삭제_성공() throws IOException {
        AccountTestUtils.createAccount(port, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD, BasicDataAccount.NICKNAME, BasicDataAccount.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD);

        given(this.spec)
            .filter(documentAccountProfileImageDelete())
            .accept(ContentType.JSON)
            .cookie(cookie)
        .when()
            .port(port)
            .delete("/api/account/profile/profile-image")
        .then()
            .statusCode(HttpStatus.OK.value());
    }*/


}