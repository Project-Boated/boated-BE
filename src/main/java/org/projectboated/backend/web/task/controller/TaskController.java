package org.projectboated.backend.web.task.controller;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.task.entity.Task;
import org.projectboated.backend.domain.task.service.TaskService;
import org.projectboated.backend.web.task.dto.CreateTaskRequest;
import org.projectboated.backend.web.task.dto.CreateTaskResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping(value = "/api/projects/{projectId}/kanban/lanes/tasks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateTaskResponse> createTask(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody CreateTaskRequest createTaskRequest
            ) {
        Task task = Task.builder()
                .name(createTaskRequest.getName())
                .description(createTaskRequest.getDescription())
                .deadline(createTaskRequest.getDeadline())
                .build();

        Task newTask = taskService.save(account, projectId, task);
        return ResponseEntity.ok(new CreateTaskResponse(newTask));
    }
}
