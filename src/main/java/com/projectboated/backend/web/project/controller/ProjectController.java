package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.domain.project.service.condition.ProjectUpdateCond;
import com.projectboated.backend.domain.task.task.service.TaskService;
import com.projectboated.backend.web.project.dto.common.ProjectCaptainResponse;
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

    @GetMapping("/{projectId}")
    public GetProjectResponse getProject(@PathVariable Long projectId) {
        Project project = projectService.findById(projectId);

        return GetProjectResponse.builder()
                .project(project)
                .taskSize(taskService.taskSize(projectId))
                .projectCaptainResponse(new ProjectCaptainResponse(project.getCaptain()))
                .build();
    }

    @PostMapping
    public CreateProjectResponse createProject(@AuthenticationPrincipal Account account,
                                               @RequestBody @Validated CreateProjectRequest request) {
        Project project = projectService.save(Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .captain(account)
                .deadline(request.getDeadline())
                .build());
        return new CreateProjectResponse(project);
    }

    @PatchMapping("/{projectId}")
    public PatchProjectResponse patchProject(@AuthenticationPrincipal Account account,
                                             @RequestBody @Validated PatchProjectRequest request,
                                             @PathVariable Long projectId) {
        ProjectUpdateCond projectUpdateCond = ProjectUpdateCond.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .build();

        projectService.update(account.getId(), projectId, projectUpdateCond);

        return new PatchProjectResponse(projectId);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.delete(projectId);
    }
}
