package com.projectboated.backend.domain.kanban.kanbanlane.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.KanbanLaneUpdateRequest;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.*;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID2;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_NAME;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_NAME2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("KanbanLane : Service 단위 테스트")
class KanbanLaneServiceTest extends ServiceTest {

    @InjectMocks
    KanbanLaneService kanbanLaneService;

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

    @Test
    void createNewLine_accountId찾을수없음_에외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.createNewLine(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_NAME))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void createNewLine_projectId찾을수없음_에외발생() {
        // Given
        Account account = createAccount(1L);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.createNewLine(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_NAME))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void createNewLine_project의captain이아닐때_에외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Account account2 = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID2)).thenReturn(Optional.of(account2));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.createNewLine(ACCOUNT_ID2, PROJECT_ID, KANBAN_LANE_NAME))
                .isInstanceOf(KanbanLaneSaveAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID2);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void createNewLine_이미lane이5개존재_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 5);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.createNewLine(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_NAME))
                .isInstanceOf(KanbanLaneAlreadyExists5.class);
    }

    @Test
    void createNewLine_정상요청_정상() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 2);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        kanbanLaneService.createNewLine(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_NAME);

        // Then
        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(kanbanLaneRepository).save(any());
    }

    @Test
    void deleteKanbanLane_프로젝트를찾을수없음_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void deleteCustomLane_lane이1개밖에없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 4);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(List.of(kanbanLanes.get(0)));

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID))
                .isInstanceOf(KanbanLaneExists1Exception.class);
    }

    @Test
    void deleteCustomLane_lane에task가있는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 4);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(List.of(kanbanLanes.get(0), kanbanLanes.get(1)));
        when(taskRepository.countByKanbanLaneId(KANBAN_LANE_ID)).thenReturn(1L);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID))
                .isInstanceOf(KanbanLaneExistsTaskException.class);
    }

    @Test
    void deleteCustomLane_KanbanLane을찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 4);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(List.of(kanbanLanes.get(0), kanbanLanes.get(1)));
        when(kanbanLaneRepository.findByIdAndProject(KANBAN_LANE_ID, project)).thenReturn(Optional.empty());
        when(taskRepository.countByKanbanLaneId(KANBAN_LANE_ID)).thenReturn(0L);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID))
                .isInstanceOf(KanbanLaneNotFoundException.class);
    }

    @Test
    void deleteCustomLane_정상입력_정상delete() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 4);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(List.of(kanbanLanes.get(0), kanbanLanes.get(1)));
        when(kanbanLaneRepository.findByIdAndProject(KANBAN_LANE_ID, project)).thenReturn(Optional.of(kanbanLanes.get(3)));
        when(taskRepository.countByKanbanLaneId(KANBAN_LANE_ID)).thenReturn(0L);

        // When
        kanbanLaneService.deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID);

        // Then
        verify(kanbanLaneRepository).delete(kanbanLanes.get(3));
    }

    @Test
    void changeTaskOrder_요청한Account찾을수없음_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .projectId(PROJECT_ID)
                .originalLaneId(0L)
                .originalTaskIndex(0)
                .changeLaneId(0L)
                .changeTaskIndex(1)
                .build();

        // Then
        assertThatThrownBy(() -> kanbanLaneService.changeTaskOrder(ACCOUNT_ID, request))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void changeTaskOrder_project찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .projectId(PROJECT_ID)
                .originalLaneId(0L)
                .originalTaskIndex(0)
                .changeLaneId(0L)
                .changeTaskIndex(1)
                .build();

        // Then
        assertThatThrownBy(() -> kanbanLaneService.changeTaskOrder(ACCOUNT_ID, request))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void changeTaskOrder_captain도아니고crew도아닌경우_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Account account = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID2)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .projectId(PROJECT_ID)
                .originalLaneId(0L)
                .originalTaskIndex(0)
                .changeLaneId(0L)
                .changeTaskIndex(1)
                .build();

        // Then
        assertThatThrownBy(() -> kanbanLaneService.changeTaskOrder(ACCOUNT_ID2, request))
                .isInstanceOf(TaskChangeOrderAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID2);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void changeTaskOrder_originalKanbanLane을찾을수없음_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 1);
        kanbanLanes.get(0).addTask(createTask(TASK_NAME));
        kanbanLanes.get(0).addTask(createTask(TASK_NAME2));

        long originalLaneId = 0L;
        long changeLaneId = 1L;

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, captain)).thenReturn(true);
        when(kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), originalLaneId)).thenReturn(Optional.empty());

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .projectId(PROJECT_ID)
                .originalLaneId(originalLaneId)
                .originalTaskIndex(0)
                .changeLaneId(changeLaneId)
                .changeTaskIndex(1)
                .build();

        // Then
        assertThatThrownBy(() -> kanbanLaneService.changeTaskOrder(ACCOUNT_ID, request))
                .isInstanceOf(KanbanLaneNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, captain);
        verify(kanbanLaneRepository).findByProjectIdAndKanbanLaneId(project.getId(), originalLaneId);
    }

    @Test
    void changeTaskOrder_changeKanbanLane을찾을수없음_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 1);
        kanbanLanes.get(0).addTask(createTask(TASK_NAME));
        kanbanLanes.get(0).addTask(createTask(TASK_NAME2));

        long originalLaneId = 0L;
        long changeLaneId = 1L;

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, captain)).thenReturn(true);
        when(kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), originalLaneId)).thenReturn(Optional.of(kanbanLanes.get(0)));
        when(kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), changeLaneId)).thenReturn(Optional.empty());

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .projectId(PROJECT_ID)
                .originalLaneId(originalLaneId)
                .originalTaskIndex(0)
                .changeLaneId(changeLaneId)
                .changeTaskIndex(1)
                .build();

        // Then
        assertThatThrownBy(() -> kanbanLaneService.changeTaskOrder(ACCOUNT_ID, request))
                .isInstanceOf(KanbanLaneNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, captain);
        verify(kanbanLaneRepository).findByProjectIdAndKanbanLaneId(project.getId(), originalLaneId);
        verify(kanbanLaneRepository).findByProjectIdAndKanbanLaneId(project.getId(), changeLaneId);
    }

    @Test
    void changeTaskOrder_정상입력_change성공() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 1);
        kanbanLanes.get(0).addTask(createTask(TASK_NAME));
        kanbanLanes.get(0).addTask(createTask(TASK_NAME2));

        long originalLaneId = 0L;
        long changeLaneId = 0L;

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, captain)).thenReturn(true);
        when(kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), originalLaneId)).thenReturn(Optional.of(kanbanLanes.get(0)));
        when(kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), changeLaneId)).thenReturn(Optional.of(kanbanLanes.get(0)));

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .projectId(PROJECT_ID)
                .originalLaneId(originalLaneId)
                .originalTaskIndex(0)
                .changeLaneId(changeLaneId)
                .changeTaskIndex(1)
                .build();

        kanbanLaneService.changeTaskOrder(ACCOUNT_ID, request);

        // Then
        assertThat(kanbanLanes.get(0).getTasks()).extracting("name")
                .containsExactly(TASK_NAME2, TASK_NAME);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, captain);
    }

    @Test
    void updateKanbanLane_accountId를찾을수없음_예외발생() {
        // Given
        String newName = "newName";
        KanbanLaneUpdateRequest request = KanbanLaneUpdateRequest.builder()
                .projectId(PROJECT_ID)
                .kanbanLaneId(0L)
                .name(newName)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.updateKanbanLane(ACCOUNT_ID, request))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void updateKanbanLane_projectId를찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        String newName = "newName";
        KanbanLaneUpdateRequest request = KanbanLaneUpdateRequest.builder()
                .projectId(PROJECT_ID)
                .kanbanLaneId(0L)
                .name(newName)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.updateKanbanLane(ACCOUNT_ID, request))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void updateKanbanLane_captain이아닌request_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Account account2 = createAccount(ACCOUNT_ID2);

        String newName = "newName";
        KanbanLaneUpdateRequest request = KanbanLaneUpdateRequest.builder()
                .projectId(PROJECT_ID)
                .kanbanLaneId(0L)
                .name(newName)
                .build();

        when(accountRepository.findById(ACCOUNT_ID2)).thenReturn(Optional.of(account2));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.updateKanbanLane(ACCOUNT_ID2, request))
                .isInstanceOf(KanbanLaneUpdateAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID2);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void updateKanbanLane_kanbanLane을찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 5);

        KanbanLaneUpdateRequest request = KanbanLaneUpdateRequest.builder()
                .projectId(PROJECT_ID)
                .kanbanLaneId(0L)
                .name("newName")
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProjectIdAndKanbanLaneId(request.getProjectId(), request.getKanbanLaneId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.updateKanbanLane(ACCOUNT_ID, request))
                .isInstanceOf(KanbanLaneNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(kanbanLaneRepository).findByProjectIdAndKanbanLaneId(request.getProjectId(), request.getKanbanLaneId());
    }

    @Test
    void updateKanbanLane_정상적인입력_정상update() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 5);

        KanbanLaneUpdateRequest request = KanbanLaneUpdateRequest.builder()
                .projectId(PROJECT_ID)
                .kanbanLaneId(0L)
                .name("newName")
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProjectIdAndKanbanLaneId(request.getProjectId(), request.getKanbanLaneId())).thenReturn(Optional.of(kanbanLanes.get(0)));

        // When
        kanbanLaneService.updateKanbanLane(ACCOUNT_ID, request);

        // Then
        assertThat(kanbanLanes.get(0).getName()).isEqualTo(request.getName());

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(kanbanLaneRepository).findByProjectIdAndKanbanLaneId(request.getProjectId(), request.getKanbanLaneId());
    }

    @Test
    void getLanes_존재하지않는프로젝트_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.getLanes(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void getLanes_4개존재_4개조회() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 5);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        List<KanbanLane> result = kanbanLaneService.getLanes(PROJECT_ID);

        // Then
        assertThat(result).isEqualTo(kanbanLanes);

        verify(projectRepository).findById(PROJECT_ID);
        verify(kanbanLaneRepository).findByProject(project);
    }

}