package com.projectboated.backend.domain.task.task.service;

import com.projectboated.backend.domain.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.domain.project.service.exception.OnlyCaptainOrCrewException;
import com.projectboated.backend.domain.task.task.service.dto.TaskUpdateRequest;
import com.projectboated.backend.domain.task.task.service.exception.*;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.task.taskfile.repository.TaskFileRepository;
import com.projectboated.backend.domain.task.tasklike.repository.TaskLikeRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneNotFoundException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.AccountTaskRepository;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final KanbanLaneRepository kanbanLaneRepository;
    private final AccountTaskRepository accountTaskRepository;
    private final TaskFileRepository taskFileRepository;
    private final TaskLikeRepository taskLikeRepository;
    private final ProjectService projectService;

    @Transactional
    public Task save(Long accountId, Long projectId, Task task) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (!projectService.isCaptainOrCrew(project, account)) {
            throw new TaskSaveAccessDeniedException();
        }

        KanbanLane kanbanLane = kanbanLaneRepository.findByKanbanAndName(project.getKanban(), "READY")
                .orElseThrow(KanbanLaneNotFoundException::new);

        task.changeProject(project);
        kanbanLane.addTask(task);

        return task;
    }

    @Transactional
    public void assignAccount(Long accountId, Long projectId, Long taskId, String nickname) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (!projectService.isCaptainOrCrew(project, account)) {
            throw new TaskAssignDeniedException();
        }

        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        Account assignAccount = accountRepository.findByNickname(nickname)
                .orElseThrow(AccountNotFoundException::new);

        if (accountTaskRepository.existsByAccountAndTask(assignAccount, task)) {
            throw new TaskAlreadyAssignedException();
        }

        accountTaskRepository.save(AccountTask.builder()
                .account(assignAccount)
                .task(task)
                .build());
    }

    public long taskSize(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        return taskRepository.countByProject(project);
    }

    @Transactional
    public void cancelAssignAccount(Long accountId, Long projectId, Long taskId, String nickname) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (!projectService.isCaptainOrCrew(project, account)) {
            throw new CancelTaskAssignDeniedException();
        }

        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        Account assignAccount = accountRepository.findByNickname(nickname)
                .orElseThrow(AccountNotFoundException::new);

        AccountTask accountTask = accountTaskRepository.findByTaskAndAccount(task, assignAccount)
                .orElseThrow(AccountTaskNotFoundException::new);

        accountTaskRepository.delete(accountTask);
    }

    public Task findById(Long accountId, Long projectId, Long taskId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (!projectService.isCaptainOrCrew(project, account)) {
            throw new TaskAccessDeniedException();
        }

        return taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);
    }

    public List<Account> findAssignedAccounts(Long accountId, Long projectId, Long taskId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (!projectService.isCaptainOrCrew(project, account)) {
            throw new OnlyCaptainOrCrewException();
        }

        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        return accountTaskRepository.findByTask(task).stream()
                .map(AccountTask::getAccount)
                .toList();
    }

    public List<UploadFile> findAssignedFiles(Long accountId, Long projectId, Long taskId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (!projectService.isCaptainOrCrew(project, account)) {
            throw new OnlyCaptainOrCrewException();
        }

        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        return taskFileRepository.findByTask(task).stream()
                .map(TaskFile::getUploadFile)
                .toList();
    }

    @OnlyCaptainOrCrew
    @Transactional
    public void updateTask(Long projectId, Long taskId, TaskUpdateRequest request) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        task.update(request);
    }

    @OnlyCaptainOrCrew
    @Transactional
    public void updateTaskLane(Long projectId, Long taskId, Long laneId) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        KanbanLane kanbanLane = kanbanLaneRepository.findById(laneId)
                .orElseThrow(KanbanLaneNotFoundException::new);
        task.changeKanbanLane(kanbanLane);
    }

    @OnlyCaptainOrCrew
    @Transactional
    public void deleteTask(Long projectId, Long taskId) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);
        taskLikeRepository.deleteByTask(task);
        taskFileRepository.deleteByTask(task);
        taskRepository.delete(task);
    }
}
