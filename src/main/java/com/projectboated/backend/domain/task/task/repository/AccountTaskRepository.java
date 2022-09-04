package com.projectboated.backend.domain.task.task.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountTaskRepository extends JpaRepository<AccountTask, Long> {
    boolean existsByAccountAndTask(Account account, Task task);

    Optional<AccountTask> findByTaskAndAccount(Task task, Account account);

    List<AccountTask> findByTask(Task task);

    List<AccountTask> findByAccount(Account account);

    @Query("""
            select at from AccountTask at inner join at.task t on at.account=:account inner join at.task.project p on at.task.project=:project
            where t.deadline is not null and t.createdDate < :nextMonth and t.deadline >= :currentMonth
            """)
    List<AccountTask> findByAccountAndProjectAndBetweenDate(Account account, Project project, LocalDateTime currentMonth, LocalDateTime nextMonth);
}
