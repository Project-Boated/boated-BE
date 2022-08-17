package com.projectboated.backend.domain.task.task.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountTaskRepository extends JpaRepository<AccountTask, Long> {
    boolean existsByAccountAndTask(Account account, Task task);

    Optional<AccountTask> findByTaskAndAccount(Task task, Account account);

    List<AccountTask> findByTask(Task task);

    List<AccountTask> findByAccount(Account account);

    @Query("""
            select at from AccountTask at inner join at.task on at.account=:account inner join at.task.project on at.task.project=:project
            """)
    List<AccountTask> findByProjectAndAccount(@Param("project") Project project, @Param("account") Account account);
}
