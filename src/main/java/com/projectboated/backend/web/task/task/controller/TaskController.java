package com.projectboated.backend.web.task.task.controller;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.service.TaskService;
import com.projectboated.backend.web.task.task.dto.PatchTaskRequest;
import com.projectboated.backend.web.task.task.dto.response.GetTaskResponse;
import com.projectboated.backend.web.task.task.dto.request.AssignAccountTaskRequest;
import com.projectboated.backend.web.task.task.dto.request.CreateTaskRequest;
import com.projectboated.backend.web.task.task.dto.response.CreateTaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/kanban/lanes/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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

        Task newTask = taskService.save(account.getId(), projectId, task);

        return new CreateTaskResponse(newTask);
    }

    @GetMapping("/{taskId}")
    public GetTaskResponse getTask(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long taskId
    ) {
        return GetTaskResponse.builder()
                .task(taskService.findById(account.getId(), projectId, taskId))
                .assignedAccounts(taskService.findAssignedAccounts(account.getId(), projectId, taskId))
                .assignedUploadFile(taskService.findAssignedFiles(account.getId(), projectId, taskId))
                .build();
    }

    @PatchMapping("/{taskId}")
    public void patchTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody PatchTaskRequest request
    ) {
        taskService.updateTask(projectId, taskId, request.toTaskUpdateRequest());
    }

    @PutMapping("/{taskId}/lanes/{laneId}")
    public void patchTaskLane(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long laneId
    ) {
        taskService.updateTaskLane(projectId, taskId, laneId);
    }

    @PostMapping(value = "/{taskId}/assign", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void assignAccountTask(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody AssignAccountTaskRequest request
    ) {
        String nickname = request.getNickname();
        taskService.assignAccount(account.getId(), projectId, taskId, nickname);
    }

    @PostMapping(value = "/{taskId}/cancel-assign", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void cancelAssignAccountTask(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody AssignAccountTaskRequest request
    ) {
        String nickname = request.getNickname();
        taskService.cancelAssignAccount(account.getId(), projectId, taskId, nickname);
    }
}
