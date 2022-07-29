package com.projectboated.backend.domain.task.tasklike.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskLikeRepository extends JpaRepository<TaskLike, Long> {
    Optional<TaskLike> findByAccountAndTask(Account account, Task task);
}
