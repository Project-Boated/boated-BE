package com.projectboated.backend.domain.kanban.kanbanlane.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneAlreadyExists5;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneSaveAccessDeniedException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("KanbanLane : Service 단위 테스트")
class KanbanLaneServiceTest extends ServiceTest {

    @InjectMocks
    KanbanLaneService kanbanLaneService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    KanbanLaneRepository kanbanLaneRepository;

    @Mock
    Account account;
    @Mock
    Project project;
    @Mock
    Kanban kanban;

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
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(project.isCaptain(account)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.createNewLine(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_NAME))
                .isInstanceOf(KanbanLaneSaveAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(project).isCaptain(account);
    }

    @Test
    void createNewLine_kanbanLane이5이상일때_에외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(project.isCaptain(account)).thenReturn(true);
        when(project.getKanban()).thenReturn(kanban);
        when(kanbanLaneRepository.countByKanban(kanban)).thenReturn(5L);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.createNewLine(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_NAME))
                .isInstanceOf(KanbanLaneAlreadyExists5.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(project).isCaptain(account);
        verify(project).getKanban();
        verify(kanbanLaneRepository).countByKanban(kanban);
    }

    @Test
    void createNewLine_정상요청_정상() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(project.isCaptain(account)).thenReturn(true);
        when(project.getKanban()).thenReturn(kanban);
        when(kanbanLaneRepository.countByKanban(kanban)).thenReturn(4L);

        // When
        kanbanLaneService.createNewLine(ACCOUNT_ID, PROJECT_ID, KANBAN_LANE_NAME);

        // Then
        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
        verify(project).isCaptain(account);
        verify(kanbanLaneRepository).countByKanban(kanban);
        verify(kanbanLaneRepository).save(any());
        verify(kanban).addKanbanLane(any());
    }


}