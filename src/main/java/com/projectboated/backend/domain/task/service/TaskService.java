package com.projectboated.backend.domain.task.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneNotFoundException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.entity.AccountTask;
import com.projectboated.backend.domain.task.entity.Task;
import com.projectboated.backend.domain.task.repository.AccountTaskRepository;
import com.projectboated.backend.domain.task.repository.TaskRepository;
import com.projectboated.backend.domain.task.service.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final KanbanLaneRepository kanbanLaneRepository;
    private final AccountTaskRepository accountTaskRepository;
    private final ProjectService projectService;

    @Transactional
    public Task save(Account account, Long projectId, Task task) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!projectService.isCaptainOrCrew(project, findAccount)) {
            throw new TaskSaveAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        KanbanLane kanbanLane = kanbanLaneRepository.findByKanbanAndName(project.getKanban(), "READY")
                .orElseThrow(() -> new KanbanLaneNotFoundException(ErrorCode.KANBAN_LANE_NOT_FOUND));

        task.changeProject(project);
        kanbanLane.addTask(task);

        return task;
    }

    @Transactional
    public void assignAccount(Account account, Long projectId, Long taskId, String nickname) {
        Account requestAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!projectService.isCaptainOrCrew(project, requestAccount)) {
            throw new TaskAssignDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(ErrorCode.TASK_NOT_FOUND));

        Account assignAccount = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (accountTaskRepository.existsByAccountAndTask(assignAccount, task)) {
            throw new TaskAlreadyAssignedException(ErrorCode.TASK_ALREADY_ASSIGNED);
        }

        AccountTask accountTask = AccountTask.builder()
                .account(assignAccount)
                .task(task)
                .build();

        accountTaskRepository.save(accountTask);
    }

    public long taskSize(Project project) {
        return taskRepository.countByProject(project);
    }

    @Transactional
    public void cancelAssignAccount(Account account, Long projectId, Long taskId, String nickname) {
        Account requestAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!projectService.isCaptainOrCrew(project, account)) {
            throw new TaskAssignDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(ErrorCode.TASK_NOT_FOUND));

        Account assignAccount = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        AccountTask accountTask = accountTaskRepository.findByTaskAndAccount(task, assignAccount)
                .orElseThrow(() -> new AccountTaskNotFoundException(ErrorCode.ACCOUNT_TASK_NOT_FOUND));

        accountTaskRepository.delete(accountTask);
    }
}
