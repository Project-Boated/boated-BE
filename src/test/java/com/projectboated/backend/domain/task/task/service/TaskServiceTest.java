package com.projectboated.backend.domain.task.task.service;

import com.projectboated.backend.utils.base.ServiceTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.kanban.kanbanlane.entity.exception.TaskChangeIndexOutOfBoundsException;
import com.projectboated.backend.kanban.kanbanlane.entity.exception.TaskOriginalIndexOutOfBoundsException;
import com.projectboated.backend.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import com.projectboated.backend.kanban.kanbanlane.service.exception.KanbanLaneNotFoundException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.AccountTaskRepository;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import com.projectboated.backend.domain.task.task.service.dto.TaskUpdateRequest;
import com.projectboated.backend.domain.task.task.service.exception.*;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.task.taskfile.repository.TaskFileRepository;
import com.projectboated.backend.domain.task.tasklike.repository.TaskLikeRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.utils.data.BasicDataKanbanLane.KANBAN_LANE_ID2;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataTask.*;
import static com.projectboated.backend.utils.data.BasicDataUploadFile.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Task : Service 단위 테스트")
class TaskServiceTest extends ServiceTest {

    @InjectMocks
    TaskService taskService;

    @Mock
    ProjectService projectService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    KanbanLaneRepository kanbanLaneRepository;
    @Mock
    TaskRepository taskRepository;
    @Mock
    AccountTaskRepository accountTaskRepository;
    @Mock
    TaskFileRepository taskFileRepository;
    @Mock
    TaskLikeRepository taskLikeRepository;

    @Test
    void findById_찾을수없는Task_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(project, kanban);
        Task task = createTask(project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findById(project.getId(), task.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void findById_정상요청인경우_return_Task() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(project, kanban);
        Task task = createTask(project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));

        // When
        Task result = taskService.findById(project.getId(), task.getId());

        // Then
        assertThat(result).isEqualTo(task);
    }

    @Test
    void findByProjectIdAndKanbanLaneId_정상적인요청_return_tasks() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(project, kanban);
        Task task = createTask(project, kanbanLane);

        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), kanbanLane.getId())).thenReturn(List.of(task));

        // When
        List<Task> result = taskService.findByProjectIdAndKanbanLaneId(project.getId(), kanbanLane.getId());

        // Then
        assertThat(result).containsExactly(task);
    }

    @Test
    void taskSize_찾을수없는project일때_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.taskSize(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void taskSize_정상Request_return_taskSize() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);

        long taskSize = 4L;

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(taskRepository.countByProject(project)).thenReturn(taskSize);

        // When
        long result = taskService.taskSize(PROJECT_ID);

        // Then
        assertThat(result).isEqualTo(taskSize);
    }

    @Test
    void save_찾을수없는Project_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(project, kanban);
        Task task = createTask(project, kanbanLane);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.save(project.getId(), task))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void save_찾을수없는KanbanLane_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(project, kanban);
        Task task = createTask(project, kanbanLane);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProjectAndFirstOrder(project)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.save(project.getId(), task))
                .isInstanceOf(KanbanLaneNotFoundException.class);
    }

    @Test
    void save_정상요청_정상save() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(project, kanban);
        Task task = createTask();

        int maxOrder = 2;
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProjectAndFirstOrder(project)).thenReturn(Optional.of(kanbanLane));
        when(taskRepository.findMaxOrder(kanbanLane)).thenReturn(maxOrder);

        // When
        taskService.save(project.getId(), task);

        // Then
        assertThat(task.getKanbanLane()).isEqualTo(kanbanLane);
        assertThat(task.getProject()).isEqualTo(project);
        assertThat(task.getOrder()).isEqualTo(maxOrder + 1);

        verify(taskRepository).save(task);
    }

    @Test
    void updateTask_task가없는경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(project, kanban);
        Task task = createTask(project, kanbanLane);

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .build();

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.updateTask(project.getId(), task.getId(), request))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void updateTask_바꾸고싶은kanbanlane이없는경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        KanbanLane kanbanLane2 = createKanbanLane(KANBAN_LANE_ID2, project, kanban);
        Task task = createTask(project, kanbanLane);

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .laneId(kanbanLane2.getId())
                .build();

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(kanbanLaneRepository.findById(request.getLaneId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.updateTask(project.getId(), task.getId(), request))
                .isInstanceOf(KanbanLaneNotFoundException.class);
    }

    @Test
    void updateTask_정상요청_정상Update() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        KanbanLane kanbanLane2 = createKanbanLane(KANBAN_LANE_ID2, project, kanban);
        Task task = createTask(project, kanbanLane);

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .name(TASK_NAME2)
                .description(TASK_DESCRIPTION2)
                .deadline(TASK_DEADLINE2)
                .laneId(kanbanLane2.getId())
                .build();

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(kanbanLaneRepository.findById(request.getLaneId())).thenReturn(Optional.of(kanbanLane2));

        // When
        taskService.updateTask(project.getId(), task.getId(), request);

        // Then
        assertThat(task.getName()).isEqualTo(TASK_NAME2);
        assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION2);
        assertThat(task.getDeadline()).isEqualTo(TASK_DEADLINE2);
        assertThat(task.getKanbanLane()).isEqualTo(kanbanLane2);
    }

    @Test
    void changeTaskOrder_originalTaskOrder가마이너스_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);

        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalTaskIndex(-1)
                .originalLaneId(kanbanLane.getId())
                .changeTaskIndex(0)
                .changeLaneId(kanbanLane.getId())
                .build();

        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.originalLaneId()))
                .thenReturn(List.of(task, task2));

        // When
        // Then
        assertThatThrownBy(() -> taskService.changeTaskOrder(project.getId(), request))
                .isInstanceOf(TaskOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_originalTaskOrder가범위를벗어남_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);

        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalTaskIndex(2)
                .originalLaneId(kanbanLane.getId())
                .changeTaskIndex(0)
                .changeLaneId(kanbanLane.getId())
                .build();

        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.originalLaneId()))
                .thenReturn(List.of(task, task2));

        // When
        // Then
        assertThatThrownBy(() -> taskService.changeTaskOrder(project.getId(), request))
                .isInstanceOf(TaskOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_kanbanlane을찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        KanbanLane kanbanLane2 = createKanbanLane(KANBAN_LANE_ID2, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);

        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalTaskIndex(0)
                .originalLaneId(kanbanLane.getId())
                .changeTaskIndex(0)
                .changeLaneId(kanbanLane2.getId())
                .build();

        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.originalLaneId()))
                .thenReturn(new ArrayList<>(List.of(task, task2)));
        when(kanbanLaneRepository.findById(request.changeLaneId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.changeTaskOrder(project.getId(), request))
                .isInstanceOf(KanbanLaneNotFoundException.class);
    }

    @Test
    void changeTaskOrder_originallane과changelane이같을때_changeTaskOrder가마이너스_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);

        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneId(kanbanLane.getId())
                .originalTaskIndex(0)
                .changeLaneId(kanbanLane.getId())
                .changeTaskIndex(-1)
                .build();

        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.originalLaneId()))
                .thenReturn(new ArrayList<>(List.of(task, task2)));

        // When
        // Then
        assertThatThrownBy(() -> taskService.changeTaskOrder(project.getId(), request))
                .isInstanceOf(TaskChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_originallane과changelane이같을때_changeTaskOrder가범위를벗어남_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);

        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneId(kanbanLane.getId())
                .originalTaskIndex(0)
                .changeLaneId(kanbanLane.getId())
                .changeTaskIndex(2)
                .build();

        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.originalLaneId()))
                .thenReturn(new ArrayList<>(List.of(task, task2)));

        // When
        // Then
        assertThatThrownBy(() -> taskService.changeTaskOrder(project.getId(), request))
                .isInstanceOf(TaskChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_originallane과changelane이같을때_정상요청_정상change() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane);

        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneId(kanbanLane.getId())
                .originalTaskIndex(0)
                .changeLaneId(kanbanLane.getId())
                .changeTaskIndex(1)
                .build();

        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.originalLaneId()))
                .thenReturn(new ArrayList<>(List.of(task, task2)));

        // When
        taskService.changeTaskOrder(project.getId(), request);

        // Then
        assertThat(task.getOrder()).isEqualTo(1);
        assertThat(task2.getOrder()).isEqualTo(0);
    }

    @Test
    void changeTaskOrder_originallane과changelane가다를때_changeTaskOrder가마이너스_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        KanbanLane kanbanLane2 = createKanbanLane(KANBAN_LANE_ID2, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane2);
        List<Task> tasks1 = new ArrayList<>();
        tasks1.add(task);
        tasks1.add(task2);

        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneId(kanbanLane.getId())
                .originalTaskIndex(0)
                .changeLaneId(kanbanLane2.getId())
                .changeTaskIndex(-1)
                .build();

        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.originalLaneId())).thenReturn(tasks1);
        when(kanbanLaneRepository.findById(request.changeLaneId())).thenReturn(Optional.of(kanbanLane2));

        // When
        // Then
        assertThatThrownBy(() -> taskService.changeTaskOrder(project.getId(), request))
                .isInstanceOf(TaskChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_originallane과changelane가다를때_changeTaskOrder가범위를벗어남_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        KanbanLane kanbanLane2 = createKanbanLane(KANBAN_LANE_ID2, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane2);

        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneId(kanbanLane.getId())
                .originalTaskIndex(0)
                .changeLaneId(kanbanLane2.getId())
                .changeTaskIndex(2)
                .build();

        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.originalLaneId())).thenReturn(new ArrayList<>(List.of(task)));
        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.changeLaneId())).thenReturn(new ArrayList<>(List.of(task2)));
        when(kanbanLaneRepository.findById(request.changeLaneId())).thenReturn(Optional.of(kanbanLane2));

        // When
        // Then
        assertThatThrownBy(() -> taskService.changeTaskOrder(project.getId(), request))
                .isInstanceOf(TaskChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_originallane과changelane가다를때_정상요청_정상change() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        KanbanLane kanbanLane2 = createKanbanLane(KANBAN_LANE_ID2, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        Task task2 = createTask(TASK_ID2, project, kanbanLane2);

        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneId(kanbanLane.getId())
                .originalTaskIndex(0)
                .changeLaneId(kanbanLane2.getId())
                .changeTaskIndex(1)
                .build();

        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.originalLaneId())).thenReturn(new ArrayList<>(List.of(task)));
        when(taskRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.changeLaneId())).thenReturn(new ArrayList<>(List.of(task2)));
        when(kanbanLaneRepository.findById(request.changeLaneId())).thenReturn(Optional.of(kanbanLane2));

        // When
        taskService.changeTaskOrder(project.getId(), request);

        // Then
        assertThat(task.getOrder()).isEqualTo(1);
        assertThat(task2.getOrder()).isEqualTo(0);
    }

    @Test
    void deleteTask_task를찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId()))
                .thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.deleteTask(project.getId(), task.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTask_정상요청_delete성공() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId()))
                .thenReturn(Optional.of(task));

        // When
        taskService.deleteTask(project.getId(), task.getId());

        // Then
        verify(taskLikeRepository).deleteByTask(task);
        verify(taskFileRepository).deleteByTask(task);
        verify(taskRepository).delete(task);
    }

    @Test
    void assignAccount_task를찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId()))
                .thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.assignAccount(project.getId(), task.getId(), account.getNickname()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void assignAccount_account를찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account.getNickname())).thenReturn(Optional.empty());


        // When
        // Then
        assertThatThrownBy(() -> taskService.assignAccount(project.getId(), task.getId(), account.getNickname()))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void assignAccount_이미assign한경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account.getNickname())).thenReturn(Optional.of(account));
        when(accountTaskRepository.existsByAccountAndTask(account, task)).thenReturn(true);


        // When
        // Then
        assertThatThrownBy(() -> taskService.assignAccount(project.getId(), task.getId(), account.getNickname()))
                .isInstanceOf(TaskAlreadyAssignedException.class);
    }

    @Test
    void assignAccount_request정상_assign정상() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account.getNickname())).thenReturn(Optional.of(account));
        when(accountTaskRepository.existsByAccountAndTask(account, task)).thenReturn(false);

        // When
        taskService.assignAccount(project.getId(), task.getId(), account.getNickname());

        // Then
        verify(accountTaskRepository).save(any());
    }

    @Test
    void cancelAssignAccount_task를찾을수없음_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.cancelAssignAccount(project.getId(), task.getId(), account.getNickname()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void cancelAssignAccount_account를찾을수없음_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account.getNickname())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.cancelAssignAccount(project.getId(), task.getId(), account.getNickname()))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void cancelAssignAccount_accountTask를찾을수없음_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account.getNickname())).thenReturn(Optional.of(account));
        when(accountTaskRepository.findByTaskAndAccount(task, account)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.cancelAssignAccount(project.getId(), task.getId(), account.getNickname()))
                .isInstanceOf(AccountTaskNotFoundException.class);
    }

    @Test
    void cancelAssignAccount_정상request_cancel성공() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        AccountTask accountTask = createAccountTask(account, task);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account.getNickname())).thenReturn(Optional.of(account));
        when(accountTaskRepository.findByTaskAndAccount(task, account)).thenReturn(Optional.of(accountTask));

        // When
        taskService.cancelAssignAccount(project.getId(), task.getId(), account.getNickname());

        // Then
        verify(accountTaskRepository).delete(accountTask);
    }

    @Test
    void findAssignedAccounts_task를찾을수없을때_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        AccountTask accountTask = createAccountTask(account, task);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findAssignedAccounts(project.getId(), task.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void findAssignedAccounts_정상요청_return_accounts() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        AccountTask accountTask = createAccountTask(account, task);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(accountTaskRepository.findByTask(task)).thenReturn(List.of(accountTask));

        // When
        List<Account> result = taskService.findAssignedAccounts(project.getId(), task.getId());

        // Then
        assertThat(result).containsExactly(account);
    }

    @Test
    void findAssignedFiles_task를찾을수없을때_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findAssignedFiles(project.getId(), task.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void findAssignedFiles_정상요청_return_uploadfiles() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        TaskFile taskFile = createTaskFile(task, uploadFile);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(taskFileRepository.findByTask(task)).thenReturn(List.of(taskFile));

        // When
        List<UploadFile> result = taskService.findAssignedFiles(project.getId(), task.getId());

        // Then
        assertThat(result).containsExactly(uploadFile);
    }
}