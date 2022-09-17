package com.projectboated.backend.domain.task.task.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountTask : Persistence 단위 테스트")
class AccountTaskRepositoryTest extends RepositoryTest {

    @Test
    void existsByAccountAndTask_acount에task가존재_return_true() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);

        insertAccountTask(account, task);

        // When
        boolean result = accountTaskRepository.existsByAccountAndTask(account, task);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByAccountAndTask_acount에다른task가있음_return_false() {
        // Given
        Account account = insertAccount(USERNAME, NICKNAME);
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        insertAccountTask(account, task);

        Account account2 = insertAccount(USERNAME2, NICKNAME2);
        Project project2 = insertProject(account);
        Kanban kanban2 = insertKanban(project);
        KanbanLane kanbanLane2 = insertKanbanLane(project2, kanban);
        Task task2 = insertTask(project2, kanbanLane);
        insertAccountTask(account, task);

        // When
        boolean result = accountTaskRepository.existsByAccountAndTask(account, task2);
        boolean result2 = accountTaskRepository.existsByAccountAndTask(account2, task);

        // Then
        assertThat(result).isFalse();
        assertThat(result2).isFalse();
    }

    @Test
    void findByTaskAndAccount_account에같은task가있음_return_AccountTask() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        insertAccountTask(account, task);

        // When
        Optional<AccountTask> result = accountTaskRepository.findByTaskAndAccount(task, account);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTask()).isEqualTo(task);
        assertThat(result.get().getAccount()).isEqualTo(account);
    }

    @Test
    void findByTaskAndAccount_account에다른task가있음_return_empty() {
        // Given
        Account account = insertAccount(USERNAME, NICKNAME);
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        insertAccountTask(account, task);

        Account account2 = insertAccount(USERNAME2, NICKNAME2);
        Project project2 = insertProject(account);
        Kanban kanban2 = insertKanban(project);
        KanbanLane kanbanLane2 = insertKanbanLane(project, kanban);
        Task task2 = insertTask(project, kanbanLane);
        insertAccountTask(account, task);

        // When
        Optional<AccountTask> result = accountTaskRepository.findByTaskAndAccount(task, account2);
        Optional<AccountTask> result2 = accountTaskRepository.findByTaskAndAccount(task2, account);

        // Then
        assertThat(result).isEmpty();
        assertThat(result2).isEmpty();
    }

    @Test
    void findByTask_task가없는경우_return_empty() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        insertAccountTask(account, task);

        // When
        List<AccountTask> result = accountTaskRepository.findByTask(new Task(912463L, "name", "Desc", null, 0));

        // Then
        assertThat(result).hasSize(0);
    }

    @Test
    void findByTask_accountTask가2개존재_return_2개() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        insertAccountTask(account, task);
        insertAccountTask(account, task);

        // When
        List<AccountTask> result = accountTaskRepository.findByTask(task);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void findByAccount_accuntTask가2개존재_return_2개() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        AccountTask accountTask = insertAccountTask(account, task);
        AccountTask accountTask2 = insertAccountTask(account, task);

        // When
        List<AccountTask> result = accountTaskRepository.findByAccount(account);

        // Then
        assertThat(result).containsExactly(accountTask, accountTask2);
    }
}