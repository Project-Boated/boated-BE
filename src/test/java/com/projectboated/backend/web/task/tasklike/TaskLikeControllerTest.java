package com.projectboated.backend.web.task.tasklike;

import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.web.config.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.common.data.BasicDataTaskFile.TASK_FILE_ID;
import static com.projectboated.backend.common.data.BasicDataUploadFile.UPLOAD_FILE_ID;
import static com.projectboated.backend.web.task.taskfile.document.TaskFileControllerDocument.documentTaskFileDelete;
import static com.projectboated.backend.web.task.tasklike.document.TaskLikeControllerDocument.documentCancelTaskLike;
import static com.projectboated.backend.web.task.tasklike.document.TaskLikeControllerDocument.documentTaskLike;
import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskLike : Controller 단위테스트")
class TaskLikeControllerTest extends ControllerTest {

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