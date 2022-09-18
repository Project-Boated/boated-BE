package com.projectboated.backend.utils.base.base;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.tasklike.entity.TaskLike;

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

    protected TaskLike createTaskLike(Long id, Account account, Task task, Integer order) {
        return TaskLike.builder()
                .id(id)
                .account(account)
                .task(task)
                .order(order)
                .build();
    }

    protected TaskLike createTaskLike(Account account, Task task, Integer order) {
        return TaskLike.builder()
                .account(account)
                .task(task)
                .order(order)
                .build();
    }

}
