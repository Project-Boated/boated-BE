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
