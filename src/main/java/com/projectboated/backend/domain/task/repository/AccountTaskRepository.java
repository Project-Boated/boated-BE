package com.projectboated.backend.domain.task.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.entity.AccountTask;
import com.projectboated.backend.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTaskRepository extends JpaRepository<AccountTask, Long> {
    boolean existsByAccountAndTask(Account account, Task task);
}
