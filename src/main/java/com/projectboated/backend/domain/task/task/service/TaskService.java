package com.projectboated.backend.domain.task.task.service;

import com.projectboated.backend.kanban.kanbanlane.entity.exception.TaskChangeIndexOutOfBoundsException;
import com.projectboated.backend.kanban.kanbanlane.entity.exception.TaskOriginalIndexOutOfBoundsException;
import com.projectboated.backend.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import com.projectboated.backend.domain.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.domain.task.task.service.dto.TaskUpdateRequest;
import com.projectboated.backend.domain.task.task.service.exception.*;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.task.taskfile.repository.TaskFileRepository;
import com.projectboated.backend.domain.task.tasklike.repository.TaskLikeRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.kanban.kanbanlane.service.exception.KanbanLaneNotFoundException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.AccountTaskRepository;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

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

    @OnlyCaptainOrCrew
    public Task findById(Long projectId, Long taskId) {
        return taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);
    }

    @OnlyCaptainOrCrew
    public List<Task> findByProjectIdAndKanbanLaneId(Long projectId, Long kanbanLaneId) {
        return taskRepository.findByProjectIdAndKanbanLaneId(projectId, kanbanLaneId);
    }

    @OnlyCaptainOrCrew
    public long taskSize(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        return taskRepository.countByProject(project);
    }

    @Transactional
    @OnlyCaptainOrCrew
    public Task save(Long projectId, Task task) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        KanbanLane kanbanLane = kanbanLaneRepository.findByProjectAndFirstOrder(project)
                .orElseThrow(KanbanLaneNotFoundException::new);

        task.changeProject(project);
        task.changeKanbanLane(kanbanLane);
        task.changeOrder(taskRepository.findMaxOrder(kanbanLane) == null ? 0 : taskRepository.findMaxOrder(kanbanLane) + 1);

        return taskRepository.save(task);
    }

    @OnlyCaptainOrCrew
    @Transactional
    public void updateTask(Long projectId, Long taskId, TaskUpdateRequest request) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        if (request.getLaneId() != null) {
            KanbanLane kanbanLane = kanbanLaneRepository.findById(request.getLaneId())
                    .orElseThrow(KanbanLaneNotFoundException::new);
            task.changeKanbanLane(kanbanLane);
        }

        task.update(request);
    }

    @Transactional
    @OnlyCaptainOrCrew
    public void changeTaskOrder(Long projectId, ChangeTaskOrderRequest request) {
        List<Task> originalTasks = taskRepository.findByProjectIdAndKanbanLaneId(projectId, request.originalLaneId());

        if (request.originalTaskIndex() < 0 || request.originalTaskIndex() >= originalTasks.size()) {
            throw new TaskOriginalIndexOutOfBoundsException();
        }
        Task target = originalTasks.remove(request.originalTaskIndex());

        List<Task> changeTasks = null;
        if (Objects.equals(request.originalLaneId(), request.changeLaneId())) {
            changeTasks = originalTasks;
        } else {
            changeTasks = taskRepository.findByProjectIdAndKanbanLaneId(projectId, request.changeLaneId());
            target.changeKanbanLane(kanbanLaneRepository.findById(request.changeLaneId())
                    .orElseThrow(KanbanLaneNotFoundException::new));
        }
        if (request.changeTaskIndex() < 0 || request.changeTaskIndex() > changeTasks.size()) {
            throw new TaskChangeIndexOutOfBoundsException();
        }

        changeTasks.add(request.changeTaskIndex(), target);
        reorderTasks(originalTasks);
        reorderTasks(changeTasks);
    }

    private void reorderTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            task.changeOrder(i);
        }
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

    @Transactional
    @OnlyCaptainOrCrew
    public void assignAccount(Long projectId, Long taskId, String nickname) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        Account account = accountRepository.findByNickname(nickname)
                .orElseThrow(AccountNotFoundException::new);

        if (accountTaskRepository.existsByAccountAndTask(account, task)) {
            throw new TaskAlreadyAssignedException();
        }

        accountTaskRepository.save(AccountTask.builder()
                .account(account)
                .task(task)
                .build());
    }

    @Transactional
    @OnlyCaptainOrCrew
    public void cancelAssignAccount(Long projectId, Long taskId, String nickname) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);
        Account account = accountRepository.findByNickname(nickname)
                .orElseThrow(AccountNotFoundException::new);

        AccountTask accountTask = accountTaskRepository.findByTaskAndAccount(task, account)
                .orElseThrow(AccountTaskNotFoundException::new);
        accountTaskRepository.delete(accountTask);
    }

    @OnlyCaptainOrCrew
    public List<Account> findAssignedAccounts(Long projectId, Long taskId) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        return accountTaskRepository.findByTask(task).stream()
                .map(AccountTask::getAccount)
                .toList();
    }

    @OnlyCaptainOrCrew
    public List<UploadFile> findAssignedFiles(Long projectId, Long taskId) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        return taskFileRepository.findByTask(task).stream()
                .map(TaskFile::getUploadFile)
                .toList();
    }
}
