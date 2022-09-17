package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.utils.basetest.ControllerTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.utils.web.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.web.project.controller.document.ProjectTerminateDocument.documentProjectCancelTerminate;
import static com.projectboated.backend.web.project.controller.document.ProjectTerminateDocument.documentProjectTerminate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Project Terminate : Controller 단위테스트")
class ProjectTerminateControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void terminateProject_프로젝트를종료시킴_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        // When
        // Then
        mockMvc.perform(post("/api/projects/{projectId}/terminate", project.getId()))
                .andExpect(status().isOk())
                .andDo(documentProjectTerminate());

        verify(projectTerminateService).terminateProject(any());
    }

    @Test
    @WithMockAccount
    void cancelTerminateProject_종료된프로젝트를시작_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        // When
        // Then
        mockMvc.perform(post("/api/projects/{projectId}/cancel-terminate", project.getId()))
                .andExpect(status().isOk())
                .andDo(documentProjectCancelTerminate());

        verify(projectTerminateService).cancelTerminateProject(any());
    }
}