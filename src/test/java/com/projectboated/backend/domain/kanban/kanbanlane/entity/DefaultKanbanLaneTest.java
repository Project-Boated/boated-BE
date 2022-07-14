package com.projectboated.backend.domain.kanban.kanbanlane.entity;

import com.projectboated.backend.common.data.BasicDataTask;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanban.entity.exception.KanbanLaneChangeIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanban.entity.exception.KanbanLaneOriginalIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.exception.TaskChangeIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.exception.TaskOriginalIndexOutOfBoundsException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("DefaultKanbanLane : Entity 단위 테스트")
class DefaultKanbanLaneTest {

    @Test
    void 생성자_DefaultKanbanLane생성_return_생성된DefaultKanbanLane() {
        // Given
        Project project = Project.builder().build();
        Kanban kanban = Kanban.builder().project(project).build();

        // When
        KanbanLane kanbanLane = new DefaultKanbanLane(KANBAN_LANE_NAME, kanban);

        // Then
        assertThat(kanbanLane.getKanban()).isEqualTo(kanban);
    }


    @Test
    void changeKanban_바꿀Kanban주어짐_changeKanban() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();

        DefaultKanbanLane dkl = DefaultKanbanLane.builder()
                .kanban(kanban)
                .build();

        Project project2 = Project.builder()
                .build();
        Kanban kanban2 = Kanban.builder()
                .project(project2)
                .build();

        // When
        dkl.changeKanban(kanban2);

        // Then
        assertThat(dkl.getKanban()).isEqualTo(kanban2);
    }

    @Test
    void addTask_추가할task주어짐_task추가() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        DefaultKanbanLane dkl = DefaultKanbanLane.builder()
                .kanban(kanban)
                .build();

        // When
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        dkl.addTask(task);

        // Then
        assertThat(task.getKanbanLane()).isEqualTo(dkl);
        assertThat(dkl.getTasks()).containsExactly(task);
    }

    @Test
    void changeTaskOrder_originalIndex가마이너스_예외발생() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        DefaultKanbanLane dkl = DefaultKanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 10)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));

        // When
        // Then
        assertThatThrownBy(() -> dkl.changeTaskOrder(-1, 4))
                .isInstanceOf(TaskOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_originalIndex가범위를벗어남_예외발생() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        DefaultKanbanLane dkl = DefaultKanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 10)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));

        // When
        // Then
        assertThatThrownBy(() -> dkl.changeTaskOrder(10, 4))
                .isInstanceOf(TaskOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_changeIndex가마이너스_예외발생() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        DefaultKanbanLane dkl = DefaultKanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 10)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));
        // When
        // Then
        assertThatThrownBy(() -> dkl.changeTaskOrder(4, -1))
                .isInstanceOf(TaskChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_changeIndex가범위를벗어남_예외발생() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        DefaultKanbanLane dkl = DefaultKanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 10)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));

        // When
        // Then
        assertThatThrownBy(() -> dkl.changeTaskOrder(8, 10))
                .isInstanceOf(TaskChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_첫번째index를끝index로옮기기_정상적으로옮겨짐() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        DefaultKanbanLane dkl = DefaultKanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 5)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));

        // When
        dkl.changeTaskOrder(0, 4);

        // Then
        assertThat(dkl.getTasks())
                .extracting("name")
                .containsExactly(TASK_NAME + 1,
                        TASK_NAME + 2,
                        TASK_NAME + 3,
                        TASK_NAME + 4,
                        TASK_NAME + 0);
    }

    @Test
    void changeTaskOrder_끝index를첫번째index로옮기기_정상적으로옮겨짐() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        DefaultKanbanLane dkl = DefaultKanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 5)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));

        // When
        dkl.changeTaskOrder(4, 0);

        // Then
        assertThat(dkl.getTasks())
                .extracting("name")
                .containsExactly(TASK_NAME + 4,
                        TASK_NAME + 0,
                        TASK_NAME + 1,
                        TASK_NAME + 2,
                        TASK_NAME + 3);
    }

}