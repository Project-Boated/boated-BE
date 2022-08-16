package com.projectboated.backend.web.task.tasklike;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.service.TaskService;
import com.projectboated.backend.domain.task.tasklike.service.TaskLikeService;
import com.projectboated.backend.web.project.dto.common.ProjectNoAccountsResponse;
import com.projectboated.backend.web.task.task.dto.common.TaskAssignedAccountResponse;
import com.projectboated.backend.web.task.task.dto.common.TaskNoLikeResponse;
import com.projectboated.backend.web.task.tasklike.dto.GetMyTaskLikeResponse;
import com.projectboated.backend.web.task.tasklike.dto.common.TaskLikeResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskLikeController {

    private final TaskService taskService;
    private final TaskLikeService taskLikeService;

    @GetMapping("/api/my/likes")
    public GetMyTaskLikeResponse getMyTaskLike(
            @AuthenticationPrincipal Account account
    ) {
        List<TaskLikeResponse> taskLikeResponses = taskLikeService.findByAccount(account.getId()).stream()
                .map(tl -> {
                    Task task = tl.getTask();
                    Project project = task.getProject();
                    ProjectNoAccountsResponse projectResponse = new ProjectNoAccountsResponse(project);

                    List<TaskAssignedAccountResponse> taskAssignedAccountResponses = taskService.findAssignedAccounts(project.getId(), task.getId()).stream()
                            .map(TaskAssignedAccountResponse::new)
                            .toList();
                    TaskNoLikeResponse taskResponse = new TaskNoLikeResponse(task, taskAssignedAccountResponses);

                    return new TaskLikeResponse(projectResponse, taskResponse);
                })
                .toList();
        return new GetMyTaskLikeResponse(taskLikeResponses);
    }

    @PostMapping("/api/my/likes/change/{originalIndex}/{changeIndex}")
    public void changeMyTaskLikeOrder(
            @AuthenticationPrincipal Account account,
            @PathVariable int originalIndex,
            @PathVariable int changeIndex
    ) {
        taskLikeService.changeOrder(account.getId(), originalIndex, changeIndex);
    }

    @PostMapping("/api/projects/{projectId}/tasks/{taskId}/like")
    public void likeTask(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long taskId
            ) {
        taskLikeService.likeTask(account.getId(), projectId, taskId);
    }

    @DeleteMapping("/api/projects/{projectId}/tasks/{taskId}/like")
    public void cancelTaskLike(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long taskId
            ) {
        taskLikeService.cancelTaskLike(account.getId(), projectId, taskId);
    }

}
