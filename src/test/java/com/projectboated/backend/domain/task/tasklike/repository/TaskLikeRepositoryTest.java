package com.projectboated.backend.domain.task.tasklike.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_ID;
import static com.projectboated.backend.common.data.BasicDataTaskLike.TASK_LIKE_ID;
import static com.projectboated.backend.common.data.BasicDataTaskLike.TASK_LIKE_ID2;
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


}