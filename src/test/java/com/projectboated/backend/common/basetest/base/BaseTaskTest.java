package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;

import static com.projectboated.backend.common.data.BasicDataTask.*;

public class BaseTaskTest extends BaseKanbanLaneTest{

    protected Task createTask(String name) {
        return Task.builder()
                .name(name)
                .build();
    }

    protected Task addTask(KanbanLane kanbanLane, String name) {
        Task task = Task.builder()
                .id(TASK_ID)
                .name(name)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        kanbanLane.addTask(task);
        return task;
    }

    protected Task addTask(KanbanLane kanbanLane, Long taskId, String name) {
        Task task = Task.builder()
                .id(taskId)
                .name(name)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        kanbanLane.addTask(task);
        return task;
    }

    protected AccountTask createAccountTask(Account account, Task task) {
        return AccountTask.builder()
                .account(account)
                .task(task)
                .build();
    }
}
