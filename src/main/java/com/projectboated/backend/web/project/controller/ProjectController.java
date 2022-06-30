package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.domain.project.condition.ProjectUpdateCond;
import com.projectboated.backend.web.project.dto.request.CreateProjectRequest;
import com.projectboated.backend.web.project.dto.request.PatchProjectRequest;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.account.entity.Account;
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
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(value = "/api/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateProjectResponse createProject(
            @AuthenticationPrincipal Account account,
            @RequestBody @Validated CreateProjectRequest createProjectRequest
    ) {
        Project project = projectService.save(
                Project.builder()
                        .name(createProjectRequest.getName())
                        .description(createProjectRequest.getDescription())
                        .captain(account)
                        .deadline(createProjectRequest.getDeadline())
                        .build()
        );
        return new CreateProjectResponse(project);
    }

    @GetMapping("/api/projects/{projectId}")
    public GetProjectResponse getProject(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        Project project = projectService.findById(projectId, account);
        return new GetProjectResponse(project);
    }

    @PatchMapping(value = "/api/projects/{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PatchProjectResponse patchProject(
            @AuthenticationPrincipal Account account,
            @RequestBody @Validated PatchProjectRequest patchProjectRequest,
            @PathVariable Long projectId
    ) {
        ProjectUpdateCond projectUpdateCond = ProjectUpdateCond.builder()
                .name(patchProjectRequest.getName())
                .description(patchProjectRequest.getDescription())
                .deadline(patchProjectRequest.getDeadline())
                .build();

        projectService.update(account, projectId, projectUpdateCond);

        return new PatchProjectResponse(projectId);
    }

    @DeleteMapping("/api/projects/{projectId}")
    public void deleteProject(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        projectService.delete(account, projectId);
    }
}
