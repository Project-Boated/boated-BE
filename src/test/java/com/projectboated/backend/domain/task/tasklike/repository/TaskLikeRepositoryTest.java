package com.projectboated.backend.domain.task.tasklike.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskLike : Persistence 단위 테스트")
class TaskLikeRepositoryTest extends RepositoryTest {

    @Test
    void findByAccountAndTask_1개존재_1개조회() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);
        Task task = insertDefaultTask(project);
        TaskLike taskLike = insertTaskLike(account, task);

        // When
        Optional<TaskLike> result = taskLikeRepository.findByAccountAndTask(account, task);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(taskLike);
    }



}