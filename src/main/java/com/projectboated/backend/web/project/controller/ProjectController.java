package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.domain.project.service.condition.ProjectUpdateCond;
import com.projectboated.backend.domain.task.task.service.TaskService;
import com.projectboated.backend.web.project.dto.request.CreateProjectRequest;
import com.projectboated.backend.web.project.dto.request.PatchProjectRequest;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.web.project.dto.response.CreateProjectResponse;
import com.projectboated.backend.web.project.dto.response.GetProjectResponse;
import com.projectboated.backend.web.project.dto.response.PatchProjectResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateProjectResponse createProject(
            @AuthenticationPrincipal Account account,
            @RequestBody @Validated CreateProjectRequest request
    ) {
        Project project = projectService.save(
                Project.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .captain(account)
                        .deadline(request.getDeadline())
                        .build()
        );
        return new CreateProjectResponse(project);
    }

    @GetMapping("/{projectId}")
    public GetProjectResponse getProject(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        Project project = projectService.findById(projectId);
        long taskSize = taskService.taskSize(project);
        return new GetProjectResponse(project,taskSize);
    }

    @PatchMapping(value = "/{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PatchProjectResponse patchProject(
            @AuthenticationPrincipal Account account,
            @RequestBody @Validated PatchProjectRequest request,
            @PathVariable Long projectId
    ) {
        ProjectUpdateCond projectUpdateCond = ProjectUpdateCond.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .build();

        projectService.update(account.getId(), projectId, projectUpdateCond);

        return new PatchProjectResponse(projectId);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        projectService.delete(account.getId(), projectId);
    }
}
