package com.projectboated.backend.task.taskfile.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import com.projectboated.backend.uploadfile.repository.UploadFileRepository;
import com.projectboated.backend.infra.aws.AwsS3Service;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.project.repository.ProjectRepository;
import com.projectboated.backend.project.service.ProjectService;
import com.projectboated.backend.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.task.repository.TaskRepository;
import com.projectboated.backend.task.task.service.exception.TaskNotFoundException;
import com.projectboated.backend.task.taskfile.entity.TaskFile;
import com.projectboated.backend.task.taskfile.repository.TaskFileRepository;
import com.projectboated.backend.task.taskfile.service.exception.TaskFileNotFoundException;
import com.projectboated.backend.utils.base.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataTask.TASK_ID;
import static com.projectboated.backend.utils.data.BasicDataTaskFile.TASK_FILE_ID;
import static com.projectboated.backend.utils.data.BasicDataUploadFile.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("TaskUploadFile : Service 단위 테스트")
class TaskUploadFileServiceTest extends ServiceTest {

    @InjectMocks
    TaskFileService taskFileService;

    @Mock
    ProjectService projectService;
    @Mock
    AwsS3Service awsS3Service;

    @Mock
    AccountRepository accountRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskFileRepository taskFileRepository;
    @Mock
    UploadFileRepository uploadFileRepository;


    @Test
    void uploadFile_찾을수없는project_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

        when(projectRepository.findById(project.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskFileService.uploadFile(project.getId(), task.getId(), multipartFile))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void uploadFile_찾을수없는task일때_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.empty());
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> taskFileService.uploadFile(project.getId(), task.getId(), multipartFile))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void uploadFile_정상요청_return_TaskUploadFile() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        // When
        taskFileService.uploadFile(project.getId(), task.getId(), multipartFile);

        // Then
        verify(taskFileRepository).save(any());
        verify(uploadFileRepository).save(any());
        verify(awsS3Service).uploadFile(any(String.class), any(MultipartFile.class));
    }

    @Test
    void delete_taskfile을찾을수없음_예외발생() {
        // Given
        when(taskFileRepository.findById(TASK_FILE_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskFileService.delete(PROJECT_ID, TASK_ID, TASK_FILE_ID))
                .isInstanceOf(TaskFileNotFoundException.class);

        verify(taskFileRepository).findById(TASK_FILE_ID);
    }

    @Test
    void delete_project를찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        TaskFile taskFile = createTaskFile(TASK_FILE_ID, task, uploadFile);

        when(taskFileRepository.findById(TASK_FILE_ID)).thenReturn(Optional.of(taskFile));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskFileService.delete(PROJECT_ID, TASK_ID, TASK_FILE_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(taskFileRepository).findById(TASK_FILE_ID);
    }

    @Test
    void delete_정상요청_delete성공() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        project.addTotalFileSize(FILE_SIZE);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        TaskFile taskFile = createTaskFile(TASK_FILE_ID, task, uploadFile);

        when(taskFileRepository.findById(TASK_FILE_ID)).thenReturn(Optional.of(taskFile));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        taskFileService.delete(PROJECT_ID, TASK_ID, TASK_FILE_ID);

        // Then
        verify(taskFileRepository).findById(TASK_FILE_ID);
        verify(taskFileRepository).delete(taskFile);
    }

    @Test
    void findById_taskfile이존재하지않음_예외발생() {
        // Given
        when(taskFileRepository.findById(TASK_FILE_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskFileService.findById(PROJECT_ID, TASK_FILE_ID))
                .isInstanceOf(TaskFileNotFoundException.class);
    }

    @Test
    void findById_정상request_return_TaskFile() {
        // Given
        TaskFile taskFile = TaskFile.builder()
                .build();

        when(taskFileRepository.findById(TASK_FILE_ID)).thenReturn(Optional.of(taskFile));

        // When
        TaskFile result = taskFileService.findById(PROJECT_ID, TASK_FILE_ID);

        // Then
        assertThat(result).isEqualTo(taskFile);
    }

}