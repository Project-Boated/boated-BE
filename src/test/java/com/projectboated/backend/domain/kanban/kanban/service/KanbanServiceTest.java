package com.projectboated.backend.domain.kanban.kanban.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanban.service.exception.KanbanAccessDeniedException;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneChangeIndexDeniedException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID2;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KanbanServiceTest extends ServiceTest {

    @InjectMocks
    KanbanService kanbanService;

    @Mock
    ProjectService projectService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    ProjectRepository projectRepository;

    @Test
    void findByProjectId_accountId로찾을수없음_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanService.findByProjectId(ACCOUNT_ID, PROJECT_ID))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void findByProjectId_projectId로찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanService.findByProjectId(ACCOUNT_ID, PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void findByProjectId_captain이나crew가아닐때_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Account account = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID2)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> kanbanService.findByProjectId(ACCOUNT_ID2, PROJECT_ID))
                .isInstanceOf(KanbanAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID2);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void findByProjectId_정상request_return_kanban() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, captain)).thenReturn(true);

        // When
        Kanban result = kanbanService.findByProjectId(ACCOUNT_ID, PROJECT_ID);

        // Then
        assertThat(result).isEqualTo(kanban);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, captain);
    }

    @Test
    void changeKanbanLaneOrder_accountId로찾을수없음_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanService.changeKanbanLaneOrder(ACCOUNT_ID, PROJECT_ID, 0, 0))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void changeKanbanLaneOrder_projectId로찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanService.changeKanbanLaneOrder(ACCOUNT_ID, PROJECT_ID, 0, 0))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void changeKanbanLaneOrder_captain이나crew가아닐때_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Account account = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID2)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> kanbanService.changeKanbanLaneOrder(ACCOUNT_ID2, PROJECT_ID, 0, 0))
                .isInstanceOf(KanbanLaneChangeIndexDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID2);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, account);
    }

    @Test
    void changeKanbanLaneOrder_정상request_changeKanbanLaneOrder() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);
        addKanbanLane(kanban, 3);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectService.isCaptainOrCrew(project, captain)).thenReturn(true);

        // When
        kanbanService.changeKanbanLaneOrder(ACCOUNT_ID, PROJECT_ID, 0, 1);

        // Then
        assertThat(kanban.getLanes()).extracting("name")
                .containsExactly(KANBAN_LANE_NAME+1, KANBAN_LANE_NAME+0, KANBAN_LANE_NAME+2);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(projectService).isCaptainOrCrew(project, captain);
    }

}