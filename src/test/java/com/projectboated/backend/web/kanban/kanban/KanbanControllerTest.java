package com.projectboated.backend.web.kanban.kanban;

import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.web.config.WithMockAccount;
import com.projectboated.backend.web.kanban.kanbanlane.dto.request.CreateKanbanLaneRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccountTask.ACCOUNT_TASK_ID;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_ID;
import static com.projectboated.backend.web.kanban.kanban.document.KanbanDocument.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Kanban : Controller 단위테스트")
class KanbanControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void getKanban_칸반lane조회_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 4);
        Task task = createTask(TASK_ID, project, kanbanLanes.get(0));
        AccountTask accountTask = createAccountTask(ACCOUNT_TASK_ID, account, task);

        when(taskLikeService.findByProjectAndAccount(account.getId(), project.getId()))
                .thenReturn(new HashMap<>());
        when(kanbanService.findByProjectId(project.getId())).thenReturn(kanban);
        when(kanbanLaneService.findByProjectId(project.getId())).thenReturn(kanbanLanes);
        when(taskService.findByProjectIdAndKanbanLaneId(project.getId(), kanbanLanes.get(0).getId())).thenReturn(List.of(task));
        when(accountTaskService.findByTask(project.getId(), task.getId())).thenReturn(List.of(accountTask));

        // When
        // Then
        mockMvc.perform(get( "/api/projects/{projectId}/kanban", PROJECT_ID))
                .andExpect(status().isOk())
                .andDo(documentKanbanRetrieve());
    }

    @Test
    @WithMockAccount
    void createCustomKanbanLane_칸반lane만들기_정상() throws Exception {
        // Given
        // When
        // Then
        mockMvc.perform(post( "/api/projects/{projectId}/kanban/lanes", PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new CreateKanbanLaneRequest(KANBAN_LANE_NAME))))
                .andExpect(status().isOk())
                .andDo(documentKanbanLaneCreate());

        verify(kanbanLaneService).createNewLine(any(), any());
    }

    @Test
    @WithMockAccount
    void deleteCustomKanbanLane_칸반lane지우기_정상() throws Exception {
        // Given
        // When
        // Then
        mockMvc.perform(delete( "/api/projects/{projectId}/kanban/lanes/{kanbanLaneId}", PROJECT_ID, KANBAN_LANE_ID))
                .andExpect(status().isOk())
                .andDo(documentKanbanLaneDelete());

        verify(kanbanLaneService).deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID);
    }

}