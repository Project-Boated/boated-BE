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
    void createNewLine_kanbanLane이5이상일때_에외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        addKanbanLane(kanban, 5);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.countByKanban(kanban)).thenReturn(5L);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.createNewLine(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_NAME))
                .isInstanceOf(KanbanLaneAlreadyExists5.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(kanbanLaneRepository).countByKanban(kanban);
    }

    @Test
    void createNewLine_정상요청_정상() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        addKanbanLane(kanban, 4);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        kanbanLaneService.createNewLine(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_NAME);

        // Then
        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(kanbanLaneRepository).countByKanban(kanban);
        verify(kanbanLaneRepository).save(any());
    }

    @Test
    void deleteCustomLane_accountId찾을수없음_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_ID))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void deleteCustomLane_projectId찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void deleteCustomLane_captain이아닐경우_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Account account = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID2)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(ACCOUNT_ID2, PROJECT_ID, KANBAN_LANE_ID))
                .isInstanceOf(KanbanLaneSaveAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID2);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void deleteCustomLane_kanbanLane이project에없는경우_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);
        addKanbanLane(kanban, 3);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_ID + 6))
                .isInstanceOf(KanbanLaneNotInProjectException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void deleteCustomLane_정상입력_정상delete() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);
        addKanbanLane(kanban, 3);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        kanbanLaneService.deleteKanbanLane(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_ID + 1);

        // Then
        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(kanbanLaneRepository).deleteById(KANBAN_LANE_ID + 1);
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
                .kanbanLaneIndex(0)
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
                .kanbanLaneIndex(0)
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
                .kanbanLaneIndex(0)
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
    void updateKanbanLane_정상적인입력_정상update() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = addKanbanLane(kanban, 5);

        String newName = "newName";
        KanbanLaneUpdateRequest request = KanbanLaneUpdateRequest.builder()
                .projectId(PROJECT_ID)
                .kanbanLaneIndex(0)
                .name(newName)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        kanbanLaneService.updateKanbanLane(ACCOUNT_ID, request);

        // Then
        assertThat(kanbanLanes.get(0).getName()).isEqualTo(newName);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

}