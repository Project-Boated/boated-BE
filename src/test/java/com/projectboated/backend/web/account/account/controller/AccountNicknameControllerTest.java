package com.projectboated.backend.web.account.account.controller;

import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.web.account.account.dto.request.PutNicknameRequest;
import com.projectboated.backend.web.account.account.dto.request.ValidationNicknameUniqueRequest;
import com.projectboated.backend.web.config.WithMockAccount;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.projectboated.backend.web.account.account.controller.document.AccountNicknameDocument.documentAccountNicknameUniqueValidation;
import static com.projectboated.backend.web.account.account.controller.document.AccountNicknameDocument.documentAccountPutNickname;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountNicknameControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void putNickname_닉네임변경_성공() throws Exception {

        // Given
        // When
        // Then
        mockMvc.perform(put("/api/account/profile/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new PutNicknameRequest("nickname2"))))
                .andExpect(status().isOk())
                .andDo(documentAccountPutNickname());

        verify(accountNicknameService).updateNickname(any(), any());
    }

    @Test
    @WithMockAccount
    void validationNicknameUnique_중복되지않는닉네임조회_notDupliated() throws Exception {
        // Given
        // When
        // Then
        mockMvc.perform(post("/api/account/profile/nickname/unique-validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new ValidationNicknameUniqueRequest("notDuplicatedNickname"))))
                .andExpect(status().isOk())
                .andDo(documentAccountNicknameUniqueValidation());

        verify(accountNicknameService).existsByNickname(any());
    }

}