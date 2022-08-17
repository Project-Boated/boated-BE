package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import com.projectboated.backend.domain.task.tasklike.repository.TaskLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskLikeRepositoryTest extends UploadFileRepositoryTest {

    @Autowired
    protected TaskLikeRepository taskLikeRepository;

    protected TaskLike insertTaskLike(Account account, Task task) {
        return taskLikeRepository.save(createTaskLike(account, task));
    }

    protected TaskLike insertTaskLike(Account account, Task task, Integer order) {
        return taskLikeRepository.save(createTaskLike(account, task, order));
    }

}
