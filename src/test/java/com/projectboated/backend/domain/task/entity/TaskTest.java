package com.projectboated.backend.domain.task.entity;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataTask.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task : Entity 단위 테스트")
class TaskTest {

    @Test
    void 생성자_Task생성_return_생성된Task() {
        // Given
        // When
        Task task = Task.builder()
						.name(TASK_NAME)
						.description(TASK_DESCRIPTION)
						.deadline(TASK_DEADLINE)
						.build();

        // Then
        assertThat(task.getName()).isEqualTo(TASK_NAME);
        assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION);
        assertThat(task.getDeadline()).isEqualTo(TASK_DEADLINE);
    }

    @Test
    void changeKanbanLane_새로운kanbanLane주어짐_kanbanlane바꿈() {
        // Given
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        KanbanLane kanbanLane = KanbanLane.builder()
                .build();

        // When
        task.changeKanbanLane(kanbanLane);

        // Then
        assertThat(task.getKanbanLane()).isEqualTo(kanbanLane);
    }

    @Test
    void changeProject_새로운Project주어짐_Project바꿈() {
        // Given
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        Project project = Project.builder()
                .build();

        // When
        task.changeProject(project);

        // Then
        assertThat(task.getProject()).isEqualTo(project);
    }

}