package org.projectboated.backend.web.project.controller;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.accountproject.service.AccountProjectService;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.service.ProjectService;
import org.projectboated.backend.domain.project.vo.ProjectUpdateCondition;
import org.projectboated.backend.security.dto.IdDto;
import org.projectboated.backend.web.project.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final AccountProjectService accountProjectService;

    @PostMapping("/api/projects")
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

    @PatchMapping("/api/projects/{projectId}")
    public PatchProjectResponse patchProject(
            @AuthenticationPrincipal Account account,
            @RequestBody @Validated PatchProjectRequest patchProjectRequest,
            @PathVariable Long projectId
    ) {
        ProjectUpdateCondition projectUpdateCondition = ProjectUpdateCondition.builder()
                .name(patchProjectRequest.getName())
                .description(patchProjectRequest.getDescription())
                .deadline(patchProjectRequest.getDeadline())
                .build();

        projectService.update(account, projectId, projectUpdateCondition);

        return new PatchProjectResponse(projectId);
    }

    @DeleteMapping("/api/projects/{projectId}")
    public void deleteProject(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        projectService.delete(account, projectId);
    }

    @GetMapping("/api/projects/my/captain")
    public GetMyCaptainProjectResponse getMyCaptainProject(@AuthenticationPrincipal Account account) {
        List<GetMyCaptainProjectResponse.ProjectResponse> projects = projectService.findAllByCaptainNotTerminated(account).stream()
                .map((p) -> new GetMyCaptainProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .collect(Collectors.toList());

        return new GetMyCaptainProjectResponse(projects);
    }

    @GetMapping("/api/projects/my/crew")
    public GetMyCrewProjectResponse getMyCrewProject(@AuthenticationPrincipal Account account) {
        List<GetMyCrewProjectResponse.ProjectResponse> projects = projectService.findAllByCrewNotTerminated(account).stream()
                .map((p) -> new GetMyCrewProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .toList();

        return new GetMyCrewProjectResponse(projects);
    }

    @GetMapping("/api/projects/my/captain/terminated")
    public GetMyCaptainTerminatedProjectResponse getMyCaptainTerminatedProject(@AuthenticationPrincipal Account account) {
        List<GetMyCaptainTerminatedProjectResponse.ProjectResponse> projects = projectService.findAllByCaptainTerminated(account).stream()
                .map((p) -> new GetMyCaptainTerminatedProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .collect(Collectors.toList());

        return new GetMyCaptainTerminatedProjectResponse(projects);
    }

    @GetMapping("/api/projects/my/crew/terminated")
    public GetMyCrewTerminatedProjectResponse getMyCrewTerminatedProject(@AuthenticationPrincipal Account account) {
        List<GetMyCrewTerminatedProjectResponse.ProjectResponse> projects = projectService.findAllByCrewTerminated(account).stream()
                .map((p) -> new GetMyCrewTerminatedProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .collect(Collectors.toList());

        return new GetMyCrewTerminatedProjectResponse(projects);
    }

    @PutMapping("/api/projects/{projectId}/captain")
    public ResponseEntity<IdDto> updateCaptain(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody UpdateCaptainRequest updateCaptainRequest) {

        Account newCaptain = projectService.updateCaptain(account, updateCaptainRequest.getNewCaptainUsername(), projectId);

        return ResponseEntity.ok(new IdDto(newCaptain.getId()));
    }

    @PostMapping("/api/projects/{projectId}/terminate")
    public TerminateProjectResponse terminateProject(@AuthenticationPrincipal Account account,
                                                     @PathVariable Long projectId) {
        projectService.terminateProject(account, projectId);

        return new TerminateProjectResponse(projectId);
    }

    @PostMapping("/api/projects/{projectId}/cancel-terminate")
    public CancelTerminateProjectResponse cancelTerminateProject(@AuthenticationPrincipal Account account,
                                                                 @PathVariable Long projectId) {
        projectService.cancelTerminateProject(account, projectId);

        return new CancelTerminateProjectResponse(projectId);
    }
}
