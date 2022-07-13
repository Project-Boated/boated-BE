package com.projectboated.backend.domain.kanban.kanban.entity;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.DefaultKanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Kanban : Entity 단위 테스트")
class KanbanTest {

    @Test
    void 생성자_Kanban생성_return_생성된Kanban() {
        // Given
        Project project = createProject();

        // When
        Kanban kanban = new Kanban(project);

        // Then
        assertThat(kanban.getProject()).isEqualTo(project);
    }

    @Test
    void changeProject_새project주어짐_project변경됨() {
        // Given
        Project project = createProject();
        Kanban kanban = new Kanban(project);

        // When
        Project newProject = createNewProject();
        kanban.changeProject(newProject);

        // Then
        assertThat(kanban.getProject()).isEqualTo(newProject);
    }

    @Test
    void addKanbanLane_새로운kanbanlane주어짐_kanbanlane추가() {
        // Given
        Project project = createProject();
        Kanban kanban = new Kanban(project);
        KanbanLane kanbanLane = DefaultKanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();

        // When
        kanban.addKanbanLane(kanbanLane);

        // Then
        assertThat(kanban.getLanes()).containsExactly(kanbanLane);
        assertThat(kanbanLane.getKanban()).isEqualTo(kanban);
    }

    private Project createProject() {
        return Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
    }

    private Project createNewProject() {
        return Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME2)
                .description(PROJECT_DESCRIPTION2)
                .deadline(PROJECT_DEADLINE2)
                .build();
    }

}