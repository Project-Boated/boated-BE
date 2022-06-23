package org.projectboated.backend.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.repository.AccountRepository;
import org.projectboated.backend.domain.exception.ErrorCode;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.exception.ProjectNotFoundException;
import org.projectboated.backend.domain.project.repository.ProjectRepository;
import org.projectboated.backend.domain.task.entity.Task;
import org.projectboated.backend.domain.task.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public void save(Account account, Long projectId, Task task) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        taskRepository.save(task);

    }
}
