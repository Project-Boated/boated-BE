package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.task.task.entity.AccountTask;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.task.repository.AccountTaskRepository;
import com.projectboated.backend.task.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskRepositoryTest extends InvitationRepositoryTest {

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected AccountTaskRepository accountTaskRepository;

    protected Task insertTask(Project project, KanbanLane kanbanLane) {
        return taskRepository.save(createTask(project, kanbanLane));
    }

    protected Task insertTask(String name, Project project, KanbanLane kanbanLane) {
        return taskRepository.save(createTask(name, project, kanbanLane));
    }

    protected AccountTask insertAccountTask(Account account, Task task) {
        return accountTaskRepository.save(createAccountTask(account, task));
    }

}
