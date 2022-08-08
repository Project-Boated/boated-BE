package com.projectboated.backend.domain.task.taskfile.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import com.projectboated.backend.domain.task.task.service.exception.TaskNotFoundException;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.task.taskfile.repository.TaskFileRepository;
import com.projectboated.backend.domain.task.taskfile.service.exception.TaskFileNotFoundException;
import com.projectboated.backend.domain.task.taskfile.service.exception.UploadTaskFileAccessDeniedException;
import com.projectboated.backend.domain.uploadfile.repository.UploadFileRepository;
import com.projectboated.backend.infra.aws.AwsS3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.common.data.BasicDataTaskFile.TASK_FILE_ID;
import static com.projectboated.backend.common.data.BasicDataUploadFile.*;
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
    void uploadFile_찾을수없는Account주어짐_예외발생() {
        // Given
        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskFileService.uploadFile(ACCOUNT_ID, PROJECT_ID, TASK_ID, multipartFile))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void uploadFile_찾을수없는Project주어짐_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskFileService.uploadFile(ACCOUNT_ID, PROJECT_ID, TASK_ID, multipartFile))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void uploadFile_captain이나crew가아니면_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);

        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> taskFileService.uploadFile(ACCOUNT_ID, PROJECT_ID, TASK_ID, multipartFile))
                .isInstanceOf(UploadTaskFileAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void uploadFile_찾을수없는task일때_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = addKanbanLane(kanban);
        Task task = addTask(kanbanLane, TASK_NAME);

        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskFileService.uploadFile(account.getId(), project.getId(), task.getId(), multipartFile))
                .isInstanceOf(TaskNotFoundException.class);

        verify(accountRepository).findById(account.getId());
        verify(projectRepository).findById(project.getId());
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(project.getId(), task.getId());
    }

    @Test
    void uploadFile_정상요청_return_TaskUploadFile() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = addKanbanLane(kanban);
        Task task = addTask(kanbanLane, TASK_NAME);

        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));

        // When
        TaskFile taskUploadFile = taskFileService.uploadFile(account.getId(), project.getId(), task.getId(), multipartFile);

        // Then
        assertThat(taskUploadFile.getTask()).isEqualTo(task);

        verify(accountRepository).findById(account.getId());
        verify(projectRepository).findById(project.getId());
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(project.getId(), task.getId());
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
    void delete_정상요청_delete성공() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = addKanbanLane(kanban);
        Task task = addTask(kanbanLane, TASK_NAME);
        TaskFile taskFile = TaskFile.builder()
                .build();

        when(taskFileRepository.findById(TASK_FILE_ID)).thenReturn(Optional.of(taskFile));

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