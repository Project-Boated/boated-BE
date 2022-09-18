package com.projectboated.backend.project.controller;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.project.controller.document.ProjectCaptainDocument;
import com.projectboated.backend.project.controller.dto.request.UpdateProjectCaptainRequest;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.utils.base.ControllerTest;
import com.projectboated.backend.utils.controller.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataAccount.NICKNAME2;
import static com.projectboated.backend.utils.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.utils.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataTask.TASK_ID;
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
                .andDo(ProjectCaptainDocument.documentProjectCaptainUpdate());

        verify(projectCaptainService).updateCaptain(any(), anyLong(), any());
    }

}