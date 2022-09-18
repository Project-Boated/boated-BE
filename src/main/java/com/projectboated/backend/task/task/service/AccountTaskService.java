package com.projectboated.backend.task.task.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.project.repository.ProjectRepository;
import com.projectboated.backend.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.task.task.entity.AccountTask;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.task.repository.AccountTaskRepository;
import com.projectboated.backend.task.task.repository.TaskRepository;
import com.projectboated.backend.task.task.service.exception.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountTaskService {

    private final AccountRepository accountRepository;
    private final TaskRepository taskRepository;
    private final AccountTaskRepository accountTaskRepository;
    private final ProjectRepository projectRepository;

    @OnlyCaptainOrCrew
    public List<AccountTask> findByTask(Long projectId, Long taskId) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);
        return accountTaskRepository.findByTask(task);
    }

    public List<AccountTask> findByProjectIdAndAccountIdAndBetweenDate(Long projectId, Long accountId, LocalDateTime targetTime) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        return accountTaskRepository.findByAccountAndProjectAndBetweenDate(account, project, targetTime, targetTime.plusMonths(1));
    }
}
