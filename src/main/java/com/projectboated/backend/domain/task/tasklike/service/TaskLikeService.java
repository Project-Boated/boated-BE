package com.projectboated.backend.domain.task.tasklike.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.exception.OnlyCaptainOrCrewException;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import com.projectboated.backend.domain.task.task.service.exception.TaskNotFoundException;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import com.projectboated.backend.domain.task.tasklike.repository.TaskLikeRepository;
import com.projectboated.backend.domain.task.tasklike.service.exception.CancelTaskLikeAccessDeniedException;
import com.projectboated.backend.domain.task.tasklike.service.exception.TaskLikeAccessDeniedException;
import com.projectboated.backend.domain.task.tasklike.service.exception.TaskLikeAlreadyExistsException;
import com.projectboated.backend.domain.task.tasklike.service.exception.TaskLikeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskLikeService {

    private final ProjectService projectService;

    private final TaskLikeRepository taskLikeRepository;
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Transactional
    @OnlyCaptainOrCrew
    public void likeTask(Long accountId, Long projectId, Long taskId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        if (taskLikeRepository.findByAccountAndTask(account, task).isPresent()) {
            throw new TaskLikeAlreadyExistsException();
        }

        TaskLike taskLike = TaskLike.builder()
                .account(account)
                .task(task)
                .build();
        taskLikeRepository.save(taskLike);
    }

    @Transactional
    @OnlyCaptainOrCrew
    public void cancelTaskLike(Long accountId, Long projectId, Long taskId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        TaskLike taskLike = taskLikeRepository.findByAccountAndTask(account, task)
                .orElseThrow(TaskLikeNotFoundException::new);
        taskLikeRepository.delete(taskLike);
    }

    @OnlyCaptainOrCrew
    public Map<Task, Boolean> findByProjectAndAccount(Long accountId, Long projectId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        Map<Task, Boolean> likes = new HashMap<>();
        for (Task task : taskRepository.findByProject(project)) {
            taskLikeRepository.findByAccountAndTask(account, task)
                    .ifPresentOrElse((tl) -> likes.put(task, true), () -> likes.put(task, false));
        }
        return likes;
    }
}
