package com.projectboated.backend.domain.task.task.service;

import com.projectboated.backend.utils.base.ServiceTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.AccountTaskRepository;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import com.projectboated.backend.domain.task.task.service.exception.TaskNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataTask.TASK_ID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("AccountTask : Service 단위 테스트")
class AccountTaskServiceTest extends ServiceTest {

    @InjectMocks
    AccountTaskService accountTaskService;

    @Mock
    TaskRepository taskRepository;
    @Mock
    AccountTaskRepository accountTaskRepository;

    @Test
    void findByTask_찾을수없는task_예외발생() {
        // Given
        when(taskRepository.findByProjectIdAndTaskId(PROJECT_ID, TASK_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> accountTaskService.findByTask(PROJECT_ID, TASK_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void findByTask_정상요청_정상return() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLane(project, kanban);
        Task task = createTask(project, kanbanLane);
        AccountTask accountTask = createAccountTask(account, task);

        when(taskRepository.findByProjectIdAndTaskId(project.getId(), task.getId())).thenReturn(Optional.of(task));
        when(accountTaskRepository.findByTask(task)).thenReturn(List.of(accountTask));

        // When
        List<AccountTask> result = accountTaskService.findByTask(project.getId(), task.getId());

        // Then
        assertThat(result).containsExactly(accountTask);
    }

}