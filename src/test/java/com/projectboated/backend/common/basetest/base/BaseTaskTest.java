package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;

import java.time.LocalDateTime;

import static com.projectboated.backend.common.data.BasicDataTask.*;

public class BaseTaskTest extends BaseKanbanLaneTest {

    protected Task createTask() {
        Task task = Task.builder()
                .id(TASK_ID)
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        task.changeCreatedDate(LocalDateTime.now());
        return task;
    }

    protected Task createTask(Project project, KanbanLane kanbanLane) {
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        task.changeProject(project);
        task.changeKanbanLane(kanbanLane);
        task.changeCreatedDate(LocalDateTime.now());
        return task;
    }

    protected Task createTask(Long id, Project project, KanbanLane kanbanLane) {
        Task task = Task.builder()
                .id(id)
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        task.changeProject(project);
        task.changeKanbanLane(kanbanLane);
        task.changeCreatedDate(LocalDateTime.now());
        return task;
    }

    protected Task createTask(Long id, String name, Project project, KanbanLane kanbanLane) {
        Task task = Task.builder()
                .id(id)
                .name(name)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        task.changeProject(project);
        task.changeKanbanLane(kanbanLane);
        task.changeCreatedDate(LocalDateTime.now());
        return task;
    }

    protected Task createTask(String name, Project project, KanbanLane kanbanLane) {
        Task task = Task.builder()
                .name(name)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        task.changeProject(project);
        task.changeKanbanLane(kanbanLane);
        task.changeCreatedDate(LocalDateTime.now());
        return task;
    }

    protected AccountTask createAccountTask(Account account, Task task) {
        return AccountTask.builder()
                .account(account)
                .task(task)
                .build();
    }

    protected AccountTask createAccountTask(Long id, Account account, Task task) {
        return AccountTask.builder()
                .id(id)
                .account(account)
                .task(task)
                .build();
    }
}
