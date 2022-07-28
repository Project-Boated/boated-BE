package com.projectboated.backend.web.task.task.controller;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.service.TaskService;
import com.projectboated.backend.web.task.task.dto.request.AssignAccountTaskRequest;
import com.projectboated.backend.web.task.task.dto.request.CreateTaskRequest;
import com.projectboated.backend.web.task.task.dto.response.CreateTaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping(value = "/api/projects/{projectId}/kanban/lanes/tasks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateTaskResponse createTask(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody CreateTaskRequest request
            ) {
        Task task = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .build();

        Task newTask = taskService.save(account, projectId, task);

        return new CreateTaskResponse(newTask);
    }

    @PostMapping(value = "/api/projects/{projectId}/kanban/lanes/tasks/{taskId}/assign", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void assignAccountTask(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody AssignAccountTaskRequest request
    ) {
        String nickname = request.getNickname();
        taskService.assignAccount(account, projectId, taskId, nickname);
    }

    @PostMapping(value = "/api/projects/{projectId}/kanban/lanes/tasks/{taskId}/cancel-assign", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void cancelAssignAccountTask(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody AssignAccountTaskRequest request
    ) {
        String nickname = request.getNickname();
        taskService.cancelAssignAccount(account, projectId, taskId, nickname);
    }
}
