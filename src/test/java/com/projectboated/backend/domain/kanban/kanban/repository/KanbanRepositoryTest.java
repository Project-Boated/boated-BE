package com.projectboated.backend.domain.kanban.kanban.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Kanban : Persistence 단위 테스트")
class KanbanRepositoryTest extends RepositoryTest {

    @Test
    void findByProjectId_프로젝트에칸반이있음_return_kanban() {
        // Given
        Project project = insertDefaultProjectAndDefaultCaptain();

        Kanban kanban = new Kanban(project);
        kanbanRepository.save(kanban);

        // When
        Optional<Kanban> byProjectId = kanbanRepository.findByProjectId(project.getId());

        // Then
        assertThat(byProjectId).isPresent();
        assertThat(byProjectId.get().getId()).isEqualTo(kanban.getId());
    }

    @Test
    void findByProjectId_프로젝트에칸반이없음_return_empty() {
        // Given
        Project project = insertDefaultProjectAndDefaultCaptain();

        // When
        Optional<Kanban> byProjectId = kanbanRepository.findByProjectId(project.getId());

        // Then
        assertThat(byProjectId).isEmpty();
    }

    @Test
    void deleteByProject_프로젝트에칸반있음_delete() {
        // Given
        Project project = insertDefaultProjectAndDefaultCaptain();

        Kanban kanban = new Kanban(project);
        kanbanRepository.save(kanban);

        // When
        kanbanRepository.deleteByProject(project);

        // Then
        assertThat(kanbanRepository.findByProjectId(project.getId())).isEmpty();
    }

}