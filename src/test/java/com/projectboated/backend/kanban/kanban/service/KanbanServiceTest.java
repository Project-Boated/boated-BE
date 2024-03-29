package com.projectboated.backend.kanban.kanban.service;

import com.projectboated.backend.utils.base.ServiceTest;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.kanban.kanban.service.exception.KanbanNotFoundException;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.project.service.exception.ProjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Kanban : Service 단위 테스트")
class KanbanServiceTest extends ServiceTest {

    @InjectMocks
    KanbanService kanbanService;

    @Mock
    ProjectRepository projectRepository;
    @Mock
    KanbanRepository kanbanRepository;

    @Test
    void findByProjectId_projectId로찾을수없음_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanService.findByProjectId(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void findByProjectId_kanban을찾을수없음_예외발생() {
        // Given
        Project project = createProjectAndCaptain(PROJECT_ID, ACCOUNT_ID);
        Kanban kanban = createKanban(KANBAN_ID, project);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(kanbanRepository.findByProject(project)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanService.findByProjectId(PROJECT_ID))
                .isInstanceOf(KanbanNotFoundException.class);
    }

    @Test
    void findByProjectId_정상request_return_kanban() {
        // Given
        Project project = createProjectAndCaptain(PROJECT_ID, ACCOUNT_ID);
        Kanban kanban = createKanban(KANBAN_ID, project);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(kanbanRepository.findByProject(project)).thenReturn(Optional.of(kanban));

        // When
        Kanban result = kanbanService.findByProjectId(PROJECT_ID);

        // Then
        assertThat(result).isEqualTo(kanban);
    }

}