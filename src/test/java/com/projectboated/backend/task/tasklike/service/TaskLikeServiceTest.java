package com.projectboated.backend.task.tasklike.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.project.service.ProjectService;
import com.projectboated.backend.project.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.task.repository.TaskRepository;
import com.projectboated.backend.task.task.service.exception.TaskNotFoundException;
import com.projectboated.backend.task.tasklike.entity.TaskLike;
import com.projectboated.backend.task.tasklike.repository.TaskLikeRepository;
import com.projectboated.backend.task.tasklike.service.exception.TaskLikeAlreadyExistsException;
import com.projectboated.backend.task.tasklike.service.exception.TaskLikeChangeIndexOutOfBoundsException;
import com.projectboated.backend.task.tasklike.service.exception.TaskLikeNotFoundException;
import com.projectboated.backend.task.tasklike.service.exception.TaskLikeOriginalIndexOutOfBoundsException;
import com.projectboated.backend.utils.base.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.utils.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataTask.TASK_ID;
import static com.projectboated.backend.utils.data.BasicDataTask.TASK_ID2;
import static com.projectboated.backend.utils.data.BasicDataTaskLike.TASK_LIKE_ID;
import static com.projectboated.backend.utils.data.BasicDataTaskLike.TASK_LIKE_ID2;
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
    void findByAccount_찾을수없는Account_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.findByAccount(ACCOUNT_ID))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void findByAccount_정상요청_정상조회() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        TaskLike taskLike = createTaskLike(account, task);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskLikeRepository.findByAccountOrderByOrder(account)).thenReturn(List.of(taskLike));

        // When
        List<TaskLike> result = taskLikeService.findByAccount(account.getId());

        // Then
        assertThat(result).contains(taskLike);
    }

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
    void likeTask_currentMaxOrder가null일경우_order가0() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(taskLikeRepository.findByAccountAndTask(account, task)).thenReturn(Optional.empty());
        when(taskLikeRepository.findByAccountMaxOrder(account)).thenReturn(null);
        when(taskLikeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        TaskLike taskLike = taskLikeService.likeTask(account.getId(), project.getId(), task.getId());

        // Then
        assertThat(taskLike.getOrder()).isEqualTo(0);

        verify(taskLikeRepository).save(any());
    }

    @Test
    void likeTask_currentMaxOrder가4일경우_order가5() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(taskLikeRepository.findByAccountAndTask(account, task)).thenReturn(Optional.empty());
        when(taskLikeRepository.findByAccountMaxOrder(account)).thenReturn(4);
        when(taskLikeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        TaskLike taskLike = taskLikeService.likeTask(account.getId(), project.getId(), task.getId());

        // Then
        assertThat(taskLike.getOrder()).isEqualTo(5);

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
        when(taskLikeRepository.findByAccountOrderByOrder(account)).thenReturn(List.of());

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
        Task task2 = createTask(TASK_ID2, project, kanbanLane);
        TaskLike taskLike = createTaskLike(TASK_LIKE_ID, account, task, 0);
        TaskLike taskLike2 = createTaskLike(TASK_LIKE_ID2, account, task2, 1);
        List<TaskLike> taskLikes = new ArrayList<>(List.of(taskLike, taskLike2));

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(taskLikeRepository.findByAccountOrderByOrder(account)).thenReturn(taskLikes);

        // When
        taskLikeService.cancelTaskLike(account.getId(), project.getId(), task.getId());

        // Then
        assertThat(taskLikes).containsExactly(taskLike2);
        assertThat(taskLikes).extracting("order")
                .containsExactly(0);

        verify(taskLikeRepository).delete(taskLike);
    }

    @Test
    void changeOrder_찾을수없는account_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.changeOrder(ACCOUNT_ID, 0, 0))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void changeOrder_originalIndex가마이너스_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);
        TaskLike taskLike = createTaskLike(TASK_LIKE_ID, account, task, 0);
        TaskLike taskLike2 = createTaskLike(TASK_LIKE_ID2, account, task2, 1);
        List<TaskLike> taskLikes = new ArrayList<>(List.of(taskLike, taskLike2));

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskLikeRepository.findByAccountOrderByOrder(account)).thenReturn(taskLikes);

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.changeOrder(account.getId(), -1, 0))
                .isInstanceOf(TaskLikeOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeOrder_originalIndex가범위를벗어남_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);
        TaskLike taskLike = createTaskLike(TASK_LIKE_ID, account, task, 0);
        TaskLike taskLike2 = createTaskLike(TASK_LIKE_ID2, account, task2, 1);
        List<TaskLike> taskLikes = new ArrayList<>(List.of(taskLike, taskLike2));

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskLikeRepository.findByAccountOrderByOrder(account)).thenReturn(taskLikes);

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.changeOrder(account.getId(), 2, 0))
                .isInstanceOf(TaskLikeOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeOrder_changeIndex가마이너스_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);
        TaskLike taskLike = createTaskLike(TASK_LIKE_ID, account, task, 0);
        TaskLike taskLike2 = createTaskLike(TASK_LIKE_ID2, account, task2, 1);
        List<TaskLike> taskLikes = new ArrayList<>(List.of(taskLike, taskLike2));

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskLikeRepository.findByAccountOrderByOrder(account)).thenReturn(taskLikes);

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.changeOrder(account.getId(), 1, -1))
                .isInstanceOf(TaskLikeChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeOrder_changeIndex가범위를벗어남_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);
        TaskLike taskLike = createTaskLike(TASK_LIKE_ID, account, task, 0);
        TaskLike taskLike2 = createTaskLike(TASK_LIKE_ID2, account, task2, 1);
        List<TaskLike> taskLikes = new ArrayList<>(List.of(taskLike, taskLike2));

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskLikeRepository.findByAccountOrderByOrder(account)).thenReturn(taskLikes);

        // When
        // Then
        assertThatThrownBy(() -> taskLikeService.changeOrder(account.getId(), 1, 2))
                .isInstanceOf(TaskLikeChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeOrder_정상요청_change정상() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);
        TaskLike taskLike = createTaskLike(TASK_LIKE_ID, account, task, 0);
        TaskLike taskLike2 = createTaskLike(TASK_LIKE_ID2, account, task2, 1);
        List<TaskLike> taskLikes = new ArrayList<>(List.of(taskLike, taskLike2));

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(taskLikeRepository.findByAccountOrderByOrder(account)).thenReturn(taskLikes);

        // When
        taskLikeService.changeOrder(account.getId(), 1, 0);

        // Then
        assertThat(taskLikes).containsExactly(taskLike2, taskLike);
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