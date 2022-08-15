package com.projectboated.backend.domain.task.task.service;

import com.projectboated.backend.domain.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.AccountTaskRepository;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import com.projectboated.backend.domain.task.task.service.exception.TaskNotFoundException;
import com.projectboated.backend.web.task.task.dto.common.TaskAssignedAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountTaskService {

    private final TaskRepository taskRepository;
    private final AccountTaskRepository accountTaskRepository;

    @OnlyCaptainOrCrew
    public List<AccountTask> findByTask(Long projectId, Long taskId) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);
        return accountTaskRepository.findByTask(task);
    }
}
