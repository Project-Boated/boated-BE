package com.projectboated.backend.invitation.controller;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.invitation.entity.Invitation;
import com.projectboated.backend.utils.base.ControllerTest;
import com.projectboated.backend.utils.controller.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.projectboated.backend.invitation.controller.document.InvitationDocument.*;
import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataInvitation.INVITATION_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Invitation : Controller 단위테스트")
class InvitationControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void inviteCrew_account초대하기_정상() throws Exception {
        // Given
        when(invitationService.inviteCrew(PROJECT_ID, "crewNickname")).thenReturn(Invitation.builder().id(123L).build());

        // When
        // Then
        mockMvc.perform(post("/api/projects/{projectId}/crews?nickname={nickname}", PROJECT_ID, "crewNickname"))
                .andExpect(status().isOk())
                .andDo(documentInvitationCreate());

        verify(invitationService).inviteCrew(any(), any());
    }

    @Test
    @WithMockAccount
    void getMyInvitation_내Invitation확인_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);

        when(invitationService.findByAccount(any())).thenReturn(List.of(createInvitation(INVITATION_ID, project, account)));

        // When
        // Then
        mockMvc.perform(get("/api/account/invitations"))
                .andExpect(status().isOk())
                .andDo(documentMyInvitationRetrieve());

        verify(invitationService).findByAccount(any());
    }

    @Test
    @WithMockAccount
    void acceptInvitation_프로젝트초대accept_정상() throws Exception {
        // Given
        when(invitationService.accept(any(), any())).thenReturn(PROJECT_ID);

        // When
        // Then
        mockMvc.perform(post("/api/account/invitations/{invitationId}/accept", INVITATION_ID))
                .andExpect(status().isOk())
                .andDo(documentInvitationAccept());

        verify(invitationService).accept(any(), any());
    }

    @Test
    @WithMockAccount
    void rejectInvitation_프로젝트초대reject_정상() throws Exception {
        // Given
        when(invitationService.reject(any(), any())).thenReturn(PROJECT_ID);

        // When
        // Then
        mockMvc.perform(post("/api/account/invitations/{invitationId}/reject", INVITATION_ID))
                .andExpect(status().isOk())
                .andDo(documentInvitationReject());

        verify(invitationService).reject(any(), any());
    }

}