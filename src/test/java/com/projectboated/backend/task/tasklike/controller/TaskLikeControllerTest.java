package com.projectboated.backend.task.tasklike.controller;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.tasklike.entity.TaskLike;
import com.projectboated.backend.utils.base.ControllerTest;
import com.projectboated.backend.utils.controller.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.projectboated.backend.task.tasklike.controller.document.TaskLikeControllerDocument.*;
import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.utils.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataTask.TASK_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskLike : Controller 단위테스트")
class TaskLikeControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void getMyTaskLike_task찾기_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        TaskLike taskLike = createTaskLike(account, task);

        when(taskLikeService.findByAccount(any())).thenReturn(List.of(taskLike));
        when(taskService.findAssignedAccounts(any(), any())).thenReturn(List.of(account));

        // When
        // Then
        mockMvc.perform(get("/api/my/likes"))
                .andExpect(status().isOk())
                .andDo(documentMyTaskLikeRetrieve());
    }

    @Test
    @WithMockAccount
    void changeMyTaskLikeOrder_task바꾸기_정상() throws Exception {
        // Given
        // When
        // Then
        mockMvc.perform(post("/api/my/likes/change/{originalIndex}/{changeIndex}", 0, 0))
                .andExpect(status().isOk())
                .andDo(documentTaskLikeOrderChange());

        verify(taskLikeService).changeOrder(any(), anyInt(), anyInt());
    }

    @Test
    @WithMockAccount
    void likeTask_하나찜하기_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        // When
        // Then
        mockMvc.perform(post("/api/projects/{projectId}/tasks/{taskId}/like", project.getId(), task.getId()))
                .andExpect(status().isOk())
                .andDo(documentTaskLike());

        verify(taskLikeService).likeTask(any(), any(), any());
    }

    @Test
    @WithMockAccount
    void cancelTaskLike_찜취소_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        // When
        // Then
        mockMvc.perform(delete("/api/projects/{projectId}/tasks/{taskId}/like", project.getId(), task.getId()))
                .andExpect(status().isOk())
                .andDo(documentCancelTaskLike());

        verify(taskLikeService).cancelTaskLike(any(), any(), any());
    }

}