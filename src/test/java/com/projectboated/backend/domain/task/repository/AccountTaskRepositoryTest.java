package com.projectboated.backend.domain.task.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.entity.AccountTask;
import com.projectboated.backend.domain.task.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountTask : Persistence 단위 테스트")
class AccountTaskRepositoryTest extends RepositoryTest {

    @Test
    void existsByAccountAndTask_acount에task가존재_return_true() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);
        Task task = insertDefaultTask(project);

        insertAccountTask(account, task);

        // When
        boolean result = accountTaskRepository.existsByAccountAndTask(account, task);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByAccountAndTask_acount에다른task가있음_return_false() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);
        Task task = insertDefaultTask(project);
        insertAccountTask(account, task);

        Account account2 = insertDefaultAccount2();
        Project project2 = insertDefaultProject2(account2);
        Task task2 = insertDefaultTask2(project2);
        insertAccountTask(account2, task2);

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
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);
        Task task = insertDefaultTask(project);

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
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);
        Task task = insertDefaultTask(project);
        insertAccountTask(account, task);

        Account account2 = insertDefaultAccount2();
        Project project2 = insertDefaultProject2(account2);
        Task task2 = insertDefaultTask2(project2);
        insertAccountTask(account2, task2);

        // When
        Optional<AccountTask> result = accountTaskRepository.findByTaskAndAccount(task, account2);
        Optional<AccountTask> result2 = accountTaskRepository.findByTaskAndAccount(task2, account);

        // Then
        assertThat(result).isEmpty();
        assertThat(result2).isEmpty();
    }

}