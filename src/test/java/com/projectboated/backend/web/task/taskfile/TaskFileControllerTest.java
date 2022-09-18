package com.projectboated.backend.web.task.taskfile;

import com.projectboated.backend.utils.base.ControllerTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.utils.controller.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.utils.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataTask.TASK_ID;
import static com.projectboated.backend.utils.data.BasicDataTaskFile.TASK_FILE_ID;
import static com.projectboated.backend.utils.data.BasicDataUploadFile.UPLOAD_FILE_ID;
import static com.projectboated.backend.web.task.taskfile.document.TaskFileControllerDocument.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("TaskFile : Controller 단위테스트")
class TaskFileControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void uploadTaskFile_파일업로드_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        TaskFile taskFile = createTaskFile(TASK_FILE_ID, task, uploadFile);

        when(taskFileService.uploadFile(any(), any(), any())).thenReturn(taskFile);

        // When
        // Then
        mockMvc.perform(multipart("/api/projects/{projectId}/tasks/{taskId}/files", project.getId(), task.getId())
                        .file(new MockMultipartFile("file", "file.txt", MediaType.TEXT_PLAIN_VALUE, "content".getBytes())))
                .andExpect(status().isOk())
                .andDo(documentUploadTaskFile());

        verify(taskFileService).uploadFile(any(), any(), any());
    }

    @Test
    @WithMockAccount
    void getTaskFile_정상요청_정상return() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        TaskFile taskFile = createTaskFile(TASK_FILE_ID, task, uploadFile);

        when(taskFileService.findById(project.getId(), taskFile.getId())).thenReturn(taskFile);
        when(awsS3Service.getBytes(taskFile.getKey())).thenReturn("File".getBytes());

        // When
        // Then
        mockMvc.perform(get("/api/projects/{projectId}/tasks/{taskId}/files/{taskFileId}", project.getId(), task.getId(), taskFile.getId()))
                .andExpect(status().isOk())
                .andDo(documentTaskFileRetrieve());
    }

    @Test
    @WithMockAccount
    void deleteTaskFile_정상요청_delete성공() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        TaskFile taskFile = createTaskFile(TASK_FILE_ID, task, uploadFile);

        // When
        // Then
        mockMvc.perform(delete("/api/projects/{projectId}/tasks/{taskId}/files/{taskFileId}", project.getId(), task.getId(), taskFile.getId()))
                .andExpect(status().isOk())
                .andDo(documentTaskFileDelete());

        verify(taskFileService).delete(project.getId(), task.getId(), taskFile.getId());
    }

}