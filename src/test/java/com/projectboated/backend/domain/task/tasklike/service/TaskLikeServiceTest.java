package com.projectboated.backend.domain.task.tasklike.service;

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
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import com.projectboated.backend.domain.task.tasklike.repository.TaskLikeRepository;
import com.projectboated.backend.domain.task.tasklike.service.exception.TaskLikeAccessDeniedException;
import com.projectboated.backend.domain.task.tasklike.service.exception.TaskLikeAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID2;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_ID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("TaskLike : Service 단위 테스트")
class TaskLikeServiceTest extends ServiceTest {

    @InjectMocks
    TaskLikeService taskLikeService;

    @Mock
    ProjectService projectService;

    @Mock
    TaskLikeRepository taskLikeRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    TaskRepository taskRepository;

    @Test
    void likeTask_없는Account_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.likeTask(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void likeTask_없는프로젝트_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.likeTask(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void likeTask_captain이나crew가아닌경우_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Account account = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID2)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.likeTask(ACCOUNT_ID2, PROJECT_ID, TASK_ID))
                .isInstanceOf(TaskLikeAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID2);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void likeTask_없는task조회_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, captain)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.likeTask(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(TaskNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, captain);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
    }

    @Test
    void likeTask_이미찜한task_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 1);
        Task task = addTask(kanbanLanes.get(0), "task");

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, captain)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(taskLikeRepository.findByAccountAndTask(captain, task)).thenReturn(Optional.of(new TaskLike(123L, captain, task)));

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.likeTask(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(TaskLikeAlreadyExistsException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, captain);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(taskLikeRepository).findByAccountAndTask(captain, task);
    }

    @Test
    void likeTask_정상Request_저장성공() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 1);
        Task task = addTask(kanbanLanes.get(0), "task");

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, captain)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(taskLikeRepository.findByAccountAndTask(captain, task)).thenReturn(Optional.empty());
        when(taskLikeRepository.save(any())).thenReturn(null);

        // When
        taskLikeService.likeTask(ACCOUNT_ID, PROJECT_ID, TASK_ID);

        // Then
        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, captain);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(taskLikeRepository).findByAccountAndTask(captain, task);
        verify(taskLikeRepository).save(any());
    }

}