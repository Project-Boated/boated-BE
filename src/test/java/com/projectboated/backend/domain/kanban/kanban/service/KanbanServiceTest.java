package com.projectboated.backend.domain.kanban.kanban.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
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
    ProjectRepository projectRepository;

    @Test
    void findByProjectId_projectId로찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanService.findByProjectId(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void findByProjectId_정상request_return_kanban() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        Kanban result = kanbanService.findByProjectId(PROJECT_ID);

        // Then
        assertThat(result).isEqualTo(kanban);

        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void changeKanbanLaneOrder_projectId로찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanService.changeKanbanLaneOrder(PROJECT_ID, 0, 0))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void changeKanbanLaneOrder_정상request_changeKanbanLaneOrder() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);
        addKanbanLane(kanban, 3);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        kanbanService.changeKanbanLaneOrder(PROJECT_ID, 0, 1);

        // Then
        assertThat(kanban.getLanes()).extracting("name")
                .containsExactly(KANBAN_LANE_NAME+1, KANBAN_LANE_NAME+0, KANBAN_LANE_NAME+2);

        verify(projectRepository).findById(PROJECT_ID);
    }

}