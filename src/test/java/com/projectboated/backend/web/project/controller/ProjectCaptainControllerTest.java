package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.web.config.WithMockAccount;
import com.projectboated.backend.web.project.dto.request.UpdateProjectCaptainRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.NICKNAME2;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_ID;
import static com.projectboated.backend.web.project.controller.document.ProjectCaptainDocument.documentProjectCaptainUpdate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Project Captain : Controller 단위테스트")
class ProjectCaptainControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void updateCaptain_capatin업데이트_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        // When
        // Then
        mockMvc.perform(put("/api/projects/{projectId}/captain", project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new UpdateProjectCaptainRequest(NICKNAME2))))
                .andExpect(status().isOk())
                .andDo(documentProjectCaptainUpdate());

        verify(projectCaptainService).updateCaptain(any(), anyLong(), any());
    }

}