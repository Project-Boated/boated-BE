package com.projectboated.backend.kanban.kanban.entity;

import com.projectboated.backend.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT;
import static com.projectboated.backend.utils.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Kanban : Entity 단위 테스트")
class KanbanTest {

    @Test
    void 생성자_Kanban생성_return_생성된Kanban() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        // When
        Kanban kanban = new Kanban(KANBAN_ID, project);

        // Then
        assertThat(kanban.getProject()).isEqualTo(project);
    }

    @Test
    void changeProject_새project주어짐_project변경됨() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();

        // When
        Project newProject = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME2)
                .description(PROJECT_DESCRIPTION2)
                .deadline(PROJECT_DEADLINE2)
                .build();
        kanban.changeProject(newProject);

        // Then
        assertThat(kanban.getProject()).isEqualTo(newProject);
    }

}