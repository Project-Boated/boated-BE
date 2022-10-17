package com.projectboated.backend.task.task.entity;

import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.task.task.service.dto.TaskUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT;
import static com.projectboated.backend.utils.data.BasicDataProject.*;
import static com.projectboated.backend.utils.data.BasicDataTask.*;
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

    @Test
    void update_모든조건null_업데이트안함() {
        // Given
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .build();

        // When
        task.update(request);

        // Then
        assertThat(task.getName()).isEqualTo(TASK_NAME);
        assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION);
        assertThat(task.getDeadline()).isEqualTo(TASK_DEADLINE);
    }

    @Test
    void update_이름이주어짐_이름만업데이트() {
        // Given
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .name(TASK_NAME2)
                .build();

        // When
        task.update(request);

        // Then
        assertThat(task.getName()).isEqualTo(TASK_NAME2);
        assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION);
        assertThat(task.getDeadline()).isEqualTo(TASK_DEADLINE);
    }

    @Test
    void update_설명이주어짐_설명만업데이트() {
        // Given
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .description(TASK_DESCRIPTION2)
                .build();

        // When
        task.update(request);

        // Then
        assertThat(task.getName()).isEqualTo(TASK_NAME);
        assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION2);
        assertThat(task.getDeadline()).isEqualTo(TASK_DEADLINE);
    }

    @Test
    void update_deadline이주어짐_deadline만업데이트() {
        // Given
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .deadline(TASK_DEADLINE2)
                .build();

        // When
        task.update(request);

        // Then
        assertThat(task.getName()).isEqualTo(TASK_NAME);
        assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION);
        assertThat(task.getDeadline()).isEqualTo(TASK_DEADLINE2);
    }

    @Test
    void update_전체가주어짐_전체업데이트() {
        // Given
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .name(TASK_NAME2)
                .description(TASK_DESCRIPTION2)
                .deadline(TASK_DEADLINE2)
                .build();

        // When
        task.update(request);

        // Then
        assertThat(task.getName()).isEqualTo(TASK_NAME2);
        assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION2);
        assertThat(task.getDeadline()).isEqualTo(TASK_DEADLINE2);
    }


    @Test
    void equals_reference가같을경우_true() {
        // Given
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        // When
        boolean result = task.equals(task);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void equals_null인경우_false() {
        // Given
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        // When
        boolean result = task.equals(null);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void equals_다른class가주어진경우_false() {
        // Given
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        // When
        boolean result = task.equals("test");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void equals_id가nulL인경우_false() {
        // Given
        Task task1 = Task.builder()
                .id(null)
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        Task task2 = Task.builder()
                .id(TASK_ID)
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        // When
        boolean result = task1.equals(task2);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void equals_같은경우_true() {
        // Given
        Task task1 = Task.builder()
                .id(TASK_ID)
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        Task task2 = Task.builder()
                .id(TASK_ID)
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        // When
        boolean result = task1.equals(task2);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void hashCode_id가null인경우_return_0() {
        // Given
        Task task1 = Task.builder()
                .id(null)
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        // When
        int result = task1.hashCode();

        // Then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void hashCode_id가null이아닌경우_return_해시값() {
        // Given
        Task task = Task.builder()
                .id(TASK_ID)
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        // When
        // Then
        int result = task.hashCode();
    }


}