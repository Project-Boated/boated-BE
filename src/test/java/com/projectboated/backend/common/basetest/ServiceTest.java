package com.projectboated.backend.common.basetest;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_DEADLINE;

@ExtendWith(MockitoExtension.class)
public class ServiceTest extends BaseTest {

    protected Account createAccount(Long id) {
        Account account = Account.builder()
                .id(id)
                .build();
        return account;
    }

    protected Project createProject(Account account) {
        return Project.builder()
                .id(PROJECT_ID)
                .captain(account)
                .build();
    }

    protected Kanban createKanban(Project project) {
        return Kanban.builder()
                .project(project)
                .build();
    }

    protected List<KanbanLane> addKanbanLane(Kanban kanban, int count) {
        return IntStream.range(0, count)
                .mapToObj((i) -> KanbanLane.builder()
                        .id(KANBAN_LANE_ID+i)
                        .name(KANBAN_LANE_NAME + i)
                        .build())
                .peek(kanban::addKanbanLane)
                .toList();
    }

    protected KanbanLane addKanbanLane(Kanban kanban) {
        KanbanLane kanbanLane = KanbanLane.builder()
                .id(KANBAN_LANE_ID)
                .name(KANBAN_LANE_NAME)
                .build();
        kanban.addKanbanLane(kanbanLane);
        return kanbanLane;
    }

    protected KanbanLane createKanbanLane(Kanban kanban) {
        return KanbanLane.builder()
                .kanban(kanban)
                .name(KANBAN_LANE_NAME)
                .build();
    }

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

    protected TaskLike createTaskLike(Account account, Task task) {
        return TaskLike.builder()
                .account(account)
                .task(task)
                .build();
    }
}
