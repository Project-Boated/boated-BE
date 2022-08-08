package com.projectboated.backend.domain.task.task.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneNotFoundException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.exception.OnlyCaptainOrCrewException;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.AccountTaskRepository;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import com.projectboated.backend.domain.task.task.service.dto.TaskUpdateRequest;
import com.projectboated.backend.domain.task.task.service.exception.*;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.task.taskfile.repository.TaskFileRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID2;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.common.data.BasicDataUploadFile.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void save_찾을수없는Account_예외발생() {
        // Given
        Task task = createTask(TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.save(ACCOUNT_ID, PROJECT_ID, task))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void save_찾을수없는Project_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.save(ACCOUNT_ID, PROJECT_ID, task))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void save_crew나captain이아닌경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> taskService.save(ACCOUNT_ID, PROJECT_ID, task))
                .isInstanceOf(TaskSaveAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void save_kanbanLane을찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(kanbanLaneRepository.findByKanbanAndName(project.getKanban(), "READY")).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.save(ACCOUNT_ID, PROJECT_ID, task))
                .isInstanceOf(KanbanLaneNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(kanbanLaneRepository).findByKanbanAndName(project.getKanban(), "READY");
    }

    @Test
    void save_정상요청_저장성공() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = createTask(TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(kanbanLaneRepository.findByKanbanAndName(project.getKanban(), "READY")).thenReturn(Optional.of(lanes.get(0)));

        // When
        taskService.save(ACCOUNT_ID, PROJECT_ID, task);

        // Then
        assertThat(task.getProject()).isEqualTo(project);
        assertThat(lanes.get(0).getTasks()).contains(task);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(kanbanLaneRepository).findByKanbanAndName(project.getKanban(), "READY");
    }

    @Test
    void assignAccount_찾을수없는Account_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.assignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account.getNickname()))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void assignAccount_찾을수없는Project_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.assignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account.getNickname()))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void assignAccount_crew나captain이아닌경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> taskService.assignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account.getNickname()))
                .isInstanceOf(TaskAssignDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void assignAccount_task를찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.assignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account.getNickname()))
                .isInstanceOf(TaskNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
    }

    @Test
    void assignAccount_nickname으로찾을수없는account인경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account.getNickname())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.assignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account.getNickname()))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(accountRepository).findByNickname(account.getNickname());
    }

    @Test
    void assignAccount_이미배정된task일경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        Account account2 = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account2.getNickname())).thenReturn(Optional.of(account2));
        when(accountTaskRepository.existsByAccountAndTask(account2, task)).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> taskService.assignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account2.getNickname()))
                .isInstanceOf(TaskAlreadyAssignedException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(accountRepository).findByNickname(account2.getNickname());
        verify(accountTaskRepository).existsByAccountAndTask(account2, task);
    }

    @Test
    void assignAccount_정상request_assign성공() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        Account account2 = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account2.getNickname())).thenReturn(Optional.of(account2));
        when(accountTaskRepository.existsByAccountAndTask(account2, task)).thenReturn(false);

        // When
        taskService.assignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account2.getNickname());

        // Then
        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(accountRepository).findByNickname(account2.getNickname());
        verify(accountTaskRepository).existsByAccountAndTask(account2, task);
        verify(accountTaskRepository).save(any());
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
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);

        long taskSize = 4L;

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(taskRepository.countByProject(project)).thenReturn(taskSize);

        // When
        long result = taskService.taskSize(PROJECT_ID);

        // Then
        assertThat(result).isEqualTo(taskSize);
    }

    @Test
    void cancelAssignAccount_찾을수없는Account_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.cancelAssignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account.getNickname()))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void cancelAssignAccount_찾을수없는Project_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.cancelAssignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account.getNickname()))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void cancelAssignAccount_crew나captain이아닌경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> taskService.cancelAssignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account.getNickname()))
                .isInstanceOf(CancelTaskAssignDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void cancelAssignAccount_task를찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.cancelAssignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account.getNickname()))
                .isInstanceOf(TaskNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
    }

    @Test
    void cancelAssignAccount_assignAccount를찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        Account account2 = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account2.getNickname())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.cancelAssignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account2.getNickname()))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(accountRepository).findByNickname(account2.getNickname());
    }

    @Test
    void cancelAssignAccount_정상Request_cancel성공() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        Account account2 = createAccount(ACCOUNT_ID2);

        AccountTask accountTask = createAccountTask(account2, task);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(accountRepository.findByNickname(account2.getNickname())).thenReturn(Optional.of(account2));
        when(accountTaskRepository.findByTaskAndAccount(task, account2)).thenReturn(Optional.of(accountTask));

        // When
        taskService.cancelAssignAccount(ACCOUNT_ID, PROJECT_ID, TASK_ID, account2.getNickname());

        // Then
        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(accountRepository).findByNickname(account2.getNickname());
        verify(accountTaskRepository).delete(accountTask);
    }

    @Test
    void findById_찾을수없는Account_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findById(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void findById_찾을수없는Project_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findById(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void findById_crew나captain이아닌경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> taskService.findById(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(TaskAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void findById_정상요청인경우_return_Task() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));

        // When
        Task result = taskService.findById(ACCOUNT_ID, PROJECT_ID, TASK_ID);

        // Then
        assertThat(result).isEqualTo(task);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
    }

    @Test
    void findAssignedAccounts_찾을수없는Account_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findAssignedAccounts(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void findAssignedAccounts_찾을수없는Project_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findAssignedAccounts(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void findAssignedAccounts_crew나captain이아닌경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> taskService.findAssignedAccounts(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(OnlyCaptainOrCrewException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void findAssignedAccounts_task를찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findAssignedAccounts(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(TaskNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
    }

    @Test
    void findAssignedAccounts_정상요청_return_accounts() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        Account account2 = createAccount(ACCOUNT_ID2);

        AccountTask accountTask = createAccountTask(account, task);
        AccountTask accountTask2 = createAccountTask(account2, task);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(accountTaskRepository.findByTask(task)).thenReturn(List.of(accountTask, accountTask2));

        // When
        List<Account> result = taskService.findAssignedAccounts(ACCOUNT_ID, PROJECT_ID, TASK_ID);

        // Then
        assertThat(result).containsExactly(account, account2);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(accountTaskRepository).findByTask(task);
    }

    @Test
    void findAssignedFiles_찾을수없는Account_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findAssignedFiles(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void findAssignedFiles_찾을수없는Project_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findAssignedFiles(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void findAssignedFiles_crew나captain이아닌경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> taskService.findAssignedFiles(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(OnlyCaptainOrCrewException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void findAssignedFiles_task를찾을수없는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.findAssignedFiles(ACCOUNT_ID, PROJECT_ID, TASK_ID))
                .isInstanceOf(TaskNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
    }

    @Test
    void findAssignedFiles_정상요청_return_uploadFiles() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        TaskFile taskFile = createTaskFile(task, uploadFile);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(true);
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(taskFileRepository.findByTask(task)).thenReturn(List.of(taskFile));

        // When
        List<UploadFile> result = taskService.findAssignedFiles(ACCOUNT_ID, PROJECT_ID, TASK_ID);

        // Then
        assertThat(result).containsExactly(uploadFile);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(taskFileRepository).findByTask(task);
    }

    @Test
    void updateTask_task가없을때_예외발생() {
        // Given
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.empty());

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .build();

        // When
        // Then
        assertThatThrownBy(() -> taskService.updateTask(PROJECT_ID, TASK_ID, request))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void updateTask_정상request_업데이트정상() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .name(TASK_NAME2)
                .description(TASK_DESCRIPTION2)
                .deadline(TASK_DEADLINE2)
                .build();

        // When
        taskService.updateTask(PROJECT_ID, TASK_ID, request);

        // Then
        assertThat(task.getName()).isEqualTo(TASK_NAME2);
        assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION2);
        assertThat(task.getDeadline()).isEqualTo(TASK_DEADLINE2);
    }

    @Test
    void updateTaskLane_찾을수없는task_예외발생() {
        // Given
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.updateTaskLane(PROJECT_ID, TASK_ID, KANBAN_LANE_ID))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
    }

    @Test
    void updateTaskLane_찾을수없는kanbanLane_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(kanbanLaneRepository.findById(KANBAN_LANE_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> taskService.updateTaskLane(PROJECT_ID, TASK_ID, KANBAN_LANE_ID))
                .isInstanceOf(KanbanLaneNotFoundException.class);

        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(kanbanLaneRepository).findById(KANBAN_LANE_ID);
    }

    @Test
    void updateTaskLane_정상요청_정상_update() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProjectAnd4Lanes(account);
        List<KanbanLane> lanes = project.getKanban().getLanes();
        Task task = addTask(lanes.get(0), TASK_NAME);

        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(kanbanLaneRepository.findById(KANBAN_LANE_ID)).thenReturn(Optional.of(lanes.get(1)));

        // When
        taskService.updateTaskLane(PROJECT_ID, TASK_ID, KANBAN_LANE_ID);

        // Then
        assertThat(task.getKanbanLane()).isEqualTo(lanes.get(1));

        verify(taskRepository).findByProjectIdAndTaskId(PROJECT_ID, TASK_ID);
        verify(kanbanLaneRepository).findById(KANBAN_LANE_ID);
    }


}