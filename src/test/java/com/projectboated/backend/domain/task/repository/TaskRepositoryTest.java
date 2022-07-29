package com.projectboated.backend.domain.task.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataTask.TASK_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task : Persistence 단위 테스트")
class TaskRepositoryTest extends RepositoryTest {

    @Test
    void countByProject_project에task2개존재_return_2개조회() {
        // Given
        Project project = insertDefaultProjectAndDefaultCaptain();
        insertDefaultTask(project);
        insertDefaultTask2(project);

        // When
        long result = taskRepository.countByProject(project);

        // Then
        assertThat(result).isEqualTo(2);
    }

    @Test
    void findByProjectIdAndTaskId_task2개존재_1개조회() {
        // Given
        Project project = insertDefaultProjectAndDefaultCaptain();
        Task task = insertDefaultTask(project);
        Task task2 = insertDefaultTask2(project);

        // When
        Optional<Task> result = taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(TASK_NAME);
    }

}