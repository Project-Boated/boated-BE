package org.projectboated.backend.web.task.controller;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.task.entity.Task;
import org.projectboated.backend.domain.task.service.TaskService;
import org.projectboated.backend.web.task.dto.CreateTaskRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/api/projects/{projectId}/tasks")
    public void createTask(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @ModelAttribute CreateTaskRequest createTaskRequest
            ) {
        Task task = Task.builder()
                .name(createTaskRequest.getName())
                .description(createTaskRequest.getDescription())
                .deadline(createTaskRequest.getDeadline())
                .build();

        taskService.save(account, projectId, task);
    }
}
