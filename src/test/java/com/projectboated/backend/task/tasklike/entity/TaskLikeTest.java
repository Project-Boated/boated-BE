package com.projectboated.backend.task.tasklike.entity;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.task.task.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataTask.TASK_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskLike : Entity 단위 테스트")
class TaskLikeTest {

    @Test
    void 생성자_TaskLike생성_return_생성된TaskLike() {
        // Given
        Account account = Account.builder()
                .build();
        Task task = Task.builder()
                .build();

        // When
        TaskLike taskLike = new TaskLike(TASK_ID, account, task, 0);

        // Then
        assertThat(taskLike.getId()).isEqualTo(TASK_ID);
        assertThat(taskLike.getAccount()).isEqualTo(account);
        assertThat(taskLike.getTask()).isEqualTo(task);
        assertThat(taskLike.getOrder()).isEqualTo(0);
    }

}