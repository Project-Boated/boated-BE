package com.projectboated.backend.domain.task.tasklike.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskLikeRepository extends JpaRepository<TaskLike, Long> {
    Optional<TaskLike> findByAccountAndTask(Account account, Task task);

    void deleteByTask(Task task);

    List<TaskLike> findByAccountOrderByOrder(Account account);

    @Query("select max(tl.order) from TaskLike tl where tl.account=:account")
    Integer findByAccountMaxOrder(@Param("account") Account account);
}
