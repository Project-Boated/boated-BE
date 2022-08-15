package com.projectboated.backend.domain.kanban.kanban.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
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