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
import com.projectboated.backend.domain.project.service.exception.OnlyCaptainOrCrewException;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import com.projectboated.backend.domain.task.task.service.exception.TaskNotFoundException;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import com.projectboated.backend.domain.task.tasklike.repository.TaskLikeRepository;
import com.projectboated.backend.domain.task.tasklike.service.exception.CancelTaskLikeAccessDeniedException;
import com.projectboated.backend.domain.task.tasklike.service.exception.TaskLikeAccessDeniedException;
import com.projectboated.backend.domain.task.tasklike.service.exception.TaskLikeAlreadyExistsException;
import com.projectboated.backend.domain.task.tasklike.service.exception.TaskLikeNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID2;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static org.assertj.core.api.Assertions.assertThat;
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
    }

    @Test
    void likeTask_없는task조회_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.likeTask(captain.getId(), project.getId(), task.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void likeTask_이미찜한task_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        TaskLike taskLike = createTaskLike(captain, task);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(taskLikeRepository.findByAccountAndTask(captain, task)).thenReturn(Optional.of(taskLike));

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.likeTask(captain.getId(), project.getId(), task.getId()))
                .isInstanceOf(TaskLikeAlreadyExistsException.class);
    }

    @Test
    void likeTask_정상Request_저장성공() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(taskLikeRepository.findByAccountAndTask(captain, task)).thenReturn(Optional.empty());

        // When
        taskLikeService.likeTask(captain.getId(), project.getId(), task.getId());

        // Then
        verify(taskLikeRepository).save(any());
    }

    @Test
    void cancelTaskLike_찾을수없는Account_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.cancelTaskLike(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void cancelTaskLike_task를찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.cancelTaskLike(account.getId(), project.getId(), task.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void cancelTaskLike_taskLike를찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(taskLikeRepository.findByAccountAndTask(account, task)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.cancelTaskLike(account.getId(), project.getId(), task.getId()))
                .isInstanceOf(TaskLikeNotFoundException.class);
    }

    @Test
    void cancelTaskLike_정상적인request_cancel성공() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        TaskLike taskLike = createTaskLike(account, task);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(taskLikeRepository.findByAccountAndTask(account, task)).thenReturn(Optional.of(taskLike));

        // When
        taskLikeService.cancelTaskLike(account.getId(), project.getId(), task.getId());

        // Then
        verify(taskLikeRepository).delete(taskLike);
    }

    @Test
    void findByProjectAndAccount_찾을수없는Account_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.findByProjectAndAccount(ACCOUNT_ID, PROJECT_ID))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void findByProjectAndAccount_찾을수없는Project_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        TaskLike taskLike = createTaskLike(account, task);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.findByProjectAndAccount(ACCOUNT_ID, PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void findByProjectAndAccount_정상요청_return_map() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        TaskLike taskLike = createTaskLike(account, task);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(taskRepository.findByProject(project)).thenReturn(List.of(task));
        when(taskLikeRepository.findByAccountAndTask(account, task)).thenReturn(Optional.of(taskLike));

        // When
        Map<Task, Boolean> result = taskLikeService.findByProjectAndAccount(ACCOUNT_ID, PROJECT_ID);

        // Then
        assertThat(result).containsEntry(task, true);
    }

}