package com.projectboated.backend.domain.task.tasklike.repository;

import com.projectboated.backend.utils.basetest.RepositoryTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskLike : Persistence 단위 테스트")
class TaskLikeRepositoryTest extends RepositoryTest {

    @Test
    void findByAccountAndTask_1개존재_1개조회() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        TaskLike taskLike = insertTaskLike(account, task);

        // When
        Optional<TaskLike> result = taskLikeRepository.findByAccountAndTask(account, task);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(taskLike);
    }

    @Test
    void deleteByTask_2개존재_2개삭제() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        insertTaskLike(account, task);
        insertTaskLike(account, task);

        // When
        taskLikeRepository.deleteByTask(task);

        // Then
        assertThat(taskLikeRepository.findByAccountAndTask(account, task)).isEmpty();
    }

    @Test
    void findByAccountOrderByOrder_2개존재_2개조회() {
        // Given
        Account account = insertAccount(USERNAME, NICKNAME);

        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        TaskLike taskLike = insertTaskLike(account, task, 0);

        Project project2 = insertProject(account);
        Kanban kanban2 = insertKanban(project2);
        KanbanLane kanbanLane2 = insertKanbanLane(project2, kanban2);
        Task task2 = insertTask(project2, kanbanLane2);
        TaskLike taskLike2 = insertTaskLike(account, task2, 1);

        // When
        List<TaskLike> result = taskLikeRepository.findByAccountOrderByOrder(account);

        // Then
        assertThat(result).containsExactly(taskLike, taskLike2);
    }

    @Test
    void findByAccountMaxOrder_tasklike가존재하지않을때_return_null() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);

        // When
        Integer result = taskLikeRepository.findByAccountMaxOrder(account);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void findByAccountMaxOrder_tasklike의order가4_return_4() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        TaskLike taskLike = insertTaskLike(account, task, 4);

        // When
        Integer result = taskLikeRepository.findByAccountMaxOrder(account);

        // Then
        assertThat(result).isEqualTo(4);
    }

}