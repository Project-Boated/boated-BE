package com.projectboated.backend.kanban.kanban.entity;

import com.projectboated.backend.project.project.entity.Project;
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

    @Test
    void equals_reference가같을경우_true() {
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
        boolean result = kanban.equals(kanban);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void equals_null인경우_false() {
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
        boolean result = kanban.equals(null);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void equals_다른class가주어진경우_false() {
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
        boolean result = kanban.equals(project);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void equals_id가nulL인경우_false() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban1 = Kanban.builder()
                .id(null)
                .project(project)
                .build();
        Kanban kanban2 = Kanban.builder()
                .id(123L)
                .project(project)
                .build();

        // When
        boolean result = kanban1.equals(kanban2);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void equals_같은경우_true() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban1 = Kanban.builder()
                .id(123L)
                .project(project)
                .build();
        Kanban kanban2 = Kanban.builder()
                .id(123L)
                .project(project)
                .build();

        // When
        boolean result = kanban1.equals(kanban2);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void hashCode_id가null인경우_return_0() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = Kanban.builder()
                .id(null)
                .project(project)
                .build();

        // When
        int result = kanban.hashCode();

        // Then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void hashCode_id가null이아닌경우_return_해시값() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = Kanban.builder()
                .id(1234L)
                .project(project)
                .build();

        // When
        // Then
        int result = kanban.hashCode();
    }

}