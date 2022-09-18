package com.projectboated.backend.task.tasklike.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.project.repository.ProjectRepository;
import com.projectboated.backend.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.task.repository.TaskRepository;
import com.projectboated.backend.task.task.service.exception.TaskNotFoundException;
import com.projectboated.backend.task.tasklike.entity.TaskLike;
import com.projectboated.backend.task.tasklike.repository.TaskLikeRepository;
import com.projectboated.backend.task.tasklike.service.exception.TaskLikeAlreadyExistsException;
import com.projectboated.backend.task.tasklike.service.exception.TaskLikeChangeIndexOutOfBoundsException;
import com.projectboated.backend.task.tasklike.service.exception.TaskLikeNotFoundException;
import com.projectboated.backend.task.tasklike.service.exception.TaskLikeOriginalIndexOutOfBoundsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskLikeService {

    private final TaskLikeRepository taskLikeRepository;
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public List<TaskLike> findByAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);
        return taskLikeRepository.findByAccountOrderByOrder(account);
    }

    @Transactional
    @OnlyCaptainOrCrew
    public TaskLike likeTask(Long accountId, Long projectId, Long taskId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        if (taskLikeRepository.findByAccountAndTask(account, task).isPresent()) {
            throw new TaskLikeAlreadyExistsException();
        }

        Integer currentMaxOrder = taskLikeRepository.findByAccountMaxOrder(account);
        Integer nextOrder = currentMaxOrder == null ? 0 : currentMaxOrder + 1;

        TaskLike taskLike = TaskLike.builder()
                .account(account)
                .task(task)
                .order(nextOrder)
                .build();
        return taskLikeRepository.save(taskLike);
    }

    @Transactional
    @OnlyCaptainOrCrew
    public void cancelTaskLike(Long accountId, Long projectId, Long taskId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        List<TaskLike> taskLikes = taskLikeRepository.findByAccountOrderByOrder(account);
        TaskLike target = null;
        for (int i = 0; i < taskLikes.size(); i++) {
            TaskLike taskLike = taskLikes.get(i);
            if (Objects.equals(taskLike.getTask().getId(), task.getId())) {
                target = taskLike;
                break;
            }
        }
        if (target == null) {
            throw new TaskLikeNotFoundException();
        }

        taskLikes.remove(target);
        taskLikeRepository.delete(target);

        reorderTaskLike(taskLikes);
    }

    @Transactional
    public void changeOrder(Long accountId, int originalIndex, int changeIndex) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        List<TaskLike> taskLikes = taskLikeRepository.findByAccountOrderByOrder(account);

        if (originalIndex < 0 || originalIndex >= taskLikes.size()) {
            throw new TaskLikeOriginalIndexOutOfBoundsException();
        }
        if (changeIndex < 0 || changeIndex >= taskLikes.size()) {
            throw new TaskLikeChangeIndexOutOfBoundsException();
        }

        TaskLike target = taskLikes.remove(originalIndex);
        taskLikes.add(changeIndex, target);
        reorderTaskLike(taskLikes);
    }

    private void reorderTaskLike(List<TaskLike> taskLikes) {
        for (int i = 0; i < taskLikes.size(); i++) {
            TaskLike taskLike = taskLikes.get(i);
            taskLike.changeOrder(i);
        }
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
