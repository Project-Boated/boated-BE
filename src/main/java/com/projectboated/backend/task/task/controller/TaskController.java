package com.projectboated.backend.task.task.controller;

import com.projectboated.backend.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import com.projectboated.backend.task.task.controller.dto.request.AssignAccountTaskRequest;
import com.projectboated.backend.task.task.controller.dto.request.CreateTaskRequest;
import com.projectboated.backend.task.task.controller.dto.request.PatchTaskRequest;
import com.projectboated.backend.task.task.controller.dto.response.CreateTaskResponse;
import com.projectboated.backend.task.task.controller.dto.response.GetTaskResponse;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/kanban/lanes/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public CreateTaskResponse createTask(
            @PathVariable Long projectId,
            @RequestBody CreateTaskRequest request
    ) {
        Task task = taskService.save(projectId,
                Task.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .deadline(request.getDeadline())
                        .build()
        );
        return new CreateTaskResponse(task);
    }

    @GetMapping("/{taskId}")
    public GetTaskResponse getTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId
    ) {
        return GetTaskResponse.builder()
                .task(taskService.findById(projectId, taskId))
                .assignedAccounts(taskService.findAssignedAccounts(projectId, taskId))
                .assignedUploadFile(taskService.findAssignedFiles(projectId, taskId))
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

    @DeleteMapping("/{taskId}")
    public void deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId
    ) {
        taskService.deleteTask(projectId, taskId);
    }

    @PostMapping(value = "/{taskId}/assign", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void assignAccountTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody AssignAccountTaskRequest request
    ) {
        String nickname = request.getNickname();
        taskService.assignAccount(projectId, taskId, nickname);
    }

    @PostMapping(value = "/{taskId}/cancel-assign", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void cancelAssignAccountTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody AssignAccountTaskRequest request
    ) {
        String nickname = request.getNickname();
        taskService.cancelAssignAccount(projectId, taskId, nickname);
    }

    @PostMapping("/change/{originalLaneId}/{originalTaskIndex}/{changeLaneId}/{changeTaskIndex}")
    public void changeTaskOrder(
            @PathVariable Long projectId,
            @PathVariable Long originalLaneId,
            @PathVariable int originalTaskIndex,
            @PathVariable Long changeLaneId,
            @PathVariable int changeTaskIndex
    ) {
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneId(originalLaneId)
                .originalTaskIndex(originalTaskIndex)
                .changeLaneId(changeLaneId)
                .changeTaskIndex(changeTaskIndex)
                .build();
        taskService.changeTaskOrder(projectId, request);
    }
}
