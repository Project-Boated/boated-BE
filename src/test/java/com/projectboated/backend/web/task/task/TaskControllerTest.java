package com.projectboated.backend.web.task.task;

import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.web.config.WithMockAccount;
import com.projectboated.backend.web.task.task.dto.PatchTaskRequest;
import com.projectboated.backend.web.task.task.dto.request.AssignAccountTaskRequest;
import com.projectboated.backend.web.task.task.dto.request.CreateTaskRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.NICKNAME2;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.common.data.BasicDataUploadFile.UPLOAD_FILE_ID;
import static com.projectboated.backend.web.task.task.document.TaskDocument.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Task : Controller 단위테스트")
class TaskControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void createTask_task하나생성_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskService.save(any(), any())).thenReturn(task);

        // When
        // Then
        mockMvc.perform(post("/api/projects/{projectId}/kanban/lanes/tasks", project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new CreateTaskRequest(TASK_NAME, TASK_DESCRIPTION, TASK_DEADLINE))))
                .andExpect(status().isOk())
                .andDo(documentTaskCreate());
    }

    @Test
    @WithMockAccount
    void getTask_조회정상_조회성공() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);

        when(taskService.findById(project.getId(), task.getId())).thenReturn(task);
        when(taskService.findAssignedAccounts(project.getId(), task.getId())).thenReturn(List.of(account));
        when(taskService.findAssignedFiles(project.getId(), task.getId())).thenReturn(List.of(uploadFile));

        // When
        // Then
        mockMvc.perform(get("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}", project.getId(), task.getId()))
                .andExpect(status().isOk())
                .andDo(documentTaskRetrieve());
    }

    @Test
    @WithMockAccount
    void patchTask_정상request_patch성공() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);

        // When
        // Then
        mockMvc.perform(patch("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}", project.getId(), task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new PatchTaskRequest(TASK_NAME, TASK_DESCRIPTION, TASK_DEADLINE, 129L)))
                )
                .andExpect(status().isOk())
                .andDo(documentTaskUpdate());
    }

    @Test
    @WithMockAccount
    void deleteTask_정상request_delete성공() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);

        // When
        // Then
        mockMvc.perform(delete("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}", project.getId(), task.getId()))
                .andExpect(status().isOk())
                .andDo(documentTaskDelete());
    }

    @Test
    @WithMockAccount
    void assignAccountTask_account하나assign_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);

        // When
        // Then
        mockMvc.perform(post("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}/assign", project.getId(), task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new AssignAccountTaskRequest(NICKNAME2))))
                .andExpect(status().isOk())
                .andDo(documentTaskAssign());
    }

    @Test
    @WithMockAccount
    void cancelAssignAccountTask_assign취소_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);

        // When
        // Then
        mockMvc.perform(post("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}/cancel-assign", project.getId(), task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new AssignAccountTaskRequest(NICKNAME2))))
                .andExpect(status().isOk())
                .andDo(documentTaskCancelAssign());

        verify(taskService).cancelAssignAccount(project.getId(), task.getId(), NICKNAME2);
    }

    @Test
    @WithMockAccount
    void changeTaskOrder_task옮기기_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);

        // When
        // Then
        mockMvc.perform(post("/api/projects/{projectId}/kanban/lanes/tasks/change/{originalLaneId}/{originalTaskIndex}/{changeLaneId}/{changeTaskIndex}", 0, 0, 0, 0, 0))
                .andExpect(status().isOk())
                .andDo(documentTaskOrderChange());

        verify(taskService).changeTaskOrder(any(), any());
    }

}