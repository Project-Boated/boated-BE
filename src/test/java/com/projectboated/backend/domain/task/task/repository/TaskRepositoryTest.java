package com.projectboated.backend.domain.task.task.repository;

import com.projectboated.backend.utils.base.RepositoryTest;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataTask.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task : Persistence 단위 테스트")
class TaskRepositoryTest extends RepositoryTest {

    @Test
    void countByProject_project에task2개존재_return_2개조회() {
        // Given
        Project project = insertProjectAndCaptain();
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        insertTask(project, kanbanLane);
        insertTask(project, kanbanLane);

        // When
        long result = taskRepository.countByProject(project);

        // Then
        assertThat(result).isEqualTo(2);
    }

    @Test
    void findByProjectIdAndTaskId_task2개존재_1개조회() {
        // Given
        Project project = insertProjectAndCaptain();
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        insertTask(project, kanbanLane);

        // When
        Optional<Task> result = taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(TASK_NAME);
    }

    @Test
    void findByProject_task2개존재_2개조회() {
        // Given
        Project project = insertProjectAndCaptain();
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        Task task2 = insertTask(project, kanbanLane);

        // When
        List<Task> result = taskRepository.findByProject(project);

        // Then
        assertThat(result).containsExactly(task, task2);
    }

    @Test
    void countByKanbanLaneId_task2개존재_2개조회() {
        // Given
        Project project = insertProjectAndCaptain();
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        insertTask(project, kanbanLane);
        insertTask(project, kanbanLane);

        // When
        Long result = taskRepository.countByKanbanLaneId(kanbanLane.getId());

        // Then
        assertThat(result).isEqualTo(2);
    }

}