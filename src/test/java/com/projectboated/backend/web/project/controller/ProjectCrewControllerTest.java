package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.web.config.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.NICKNAME2;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.web.project.controller.document.ProjectCrewDocument.documentProjectsCrewDelete;
import static com.projectboated.backend.web.project.controller.document.ProjectCrewDocument.documentProjectsCrewsRetrieve;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Project Crew : Controller 단위테스트")
class ProjectCrewControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void getCrews_project에속한모든crew조회_성공() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        when(projectCrewService.findAllCrews(anyLong())).thenReturn(List.of(createAccount(ACCOUNT_ID)));

        // When
        // Then
        mockMvc.perform(get("/api/projects/{projectId}/crews", project.getId()))
                .andExpect(status().isOk())
                .andDo(documentProjectsCrewsRetrieve());
    }

    @Test
    @WithMockAccount
    void deleteCrew_project에속한모든crew추방_성공() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        // When
        // Then
        mockMvc.perform(delete("/api/projects/{projectId}/crews/{crewNickname}", project.getId(), NICKNAME2))
                .andExpect(status().isOk())
                .andDo(documentProjectsCrewDelete());

        verify(projectCrewService).deleteCrew(any(), any());
    }

}