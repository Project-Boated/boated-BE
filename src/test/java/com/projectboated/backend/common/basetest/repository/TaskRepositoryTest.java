package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.AccountTaskRepository;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static com.projectboated.backend.common.data.BasicDataTask.*;

public class TaskRepositoryTest extends InvitationRepositoryTest {

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected AccountTaskRepository accountTaskRepository;

    protected Task insertDefaultTask(Project project) {
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        task.changeProject(project);
        return taskRepository.save(task);
    }

    protected Task insertDefaultTask2(Project project) {
        Task task = Task.builder()
                .name(TASK_NAME2)
                .description(TASK_DESCRIPTION2)
                .deadline(TASK_DEADLINE2)
                .build();
        task.changeProject(project);
        return taskRepository.save(task);
    }

    protected AccountTask insertAccountTask(Account account, Task task) {
        return accountTaskRepository.save(AccountTask.builder()
                .account(account)
                .task(task)
                .build());
    }

    protected Task insertDefaultTask(KanbanLane kl) {
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();
        task.changeKanbanLane(kl);
        return taskRepository.save(task);
    }

    protected Task insertDefaultTask2(KanbanLane kl) {
        Task task = Task.builder()
                .name(TASK_NAME2)
                .description(TASK_DESCRIPTION2)
                .deadline(TASK_DEADLINE2)
                .build();
        task.changeKanbanLane(kl);
        return taskRepository.save(task);
    }

}
