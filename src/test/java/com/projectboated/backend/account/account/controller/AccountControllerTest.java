package com.projectboated.backend.account.account.controller;

import com.google.common.net.MediaType;
import com.projectboated.backend.account.account.controller.dto.request.SignUpRequest;
import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.web.config.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;

import static com.projectboated.backend.account.account.controller.document.AccountDocument.*;
import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
        when(accountService.findById(anyLong())).thenReturn(createAccount());
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
        when(accountService.findById(anyLong())).thenReturn(createAccount());
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
}