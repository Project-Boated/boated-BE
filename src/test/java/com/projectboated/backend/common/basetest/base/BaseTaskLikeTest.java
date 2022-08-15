package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;

import static com.projectboated.backend.common.data.BasicDataTaskLike.TASK_LIKE_ID;

public class BaseTaskLikeTest extends BaseTaskFileTest {

    protected TaskLike createTaskLike(Account account, Task task) {
        return TaskLike.builder()
                .account(account)
                .task(task)
                .build();
    }

    protected TaskLike createTaskLike(Long id, Account account, Task task) {
        return TaskLike.builder()
                .id(id)
                .account(account)
                .task(task)
                .build();
    }

}
