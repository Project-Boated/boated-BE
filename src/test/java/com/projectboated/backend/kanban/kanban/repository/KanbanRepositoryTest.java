package com.projectboated.backend.kanban.kanban.repository;

import com.projectboated.backend.utils.base.RepositoryTest;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Kanban : Persistence 단위 테스트")
class KanbanRepositoryTest extends RepositoryTest {

    @Test
    void deleteByProject_프로젝트에칸반있음_delete() {
        // Given
        Project project = insertProjectAndCaptain();
        Kanban kanban = insertKanban(project);

        // When
        kanbanRepository.deleteByProject(project);
        flushAndClear();

        // Then
        assertThat(kanbanRepository.findById(kanban.getId())).isEmpty();
    }

}