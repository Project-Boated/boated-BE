package my.sleepydeveloper.projectcompass.web.project.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.invitation.entity.Invitation;
import my.sleepydeveloper.projectcompass.domain.invitation.InvitationService;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.service.ProjectService;
import my.sleepydeveloper.projectcompass.domain.project.vo.ProjectUpdateCondition;
import my.sleepydeveloper.projectcompass.security.dto.IdDto;
import my.sleepydeveloper.projectcompass.web.project.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final InvitationService invitationService;

    @PostMapping
    public CreateProjectResponse createProject(
            @AuthenticationPrincipal Account account,
            @RequestBody @Validated CreateProjectRequest createProjectRequest
    ) {
        Project project = projectService.save(new Project(createProjectRequest.getName(), createProjectRequest.getDescription(), account));
        return new CreateProjectResponse(project);
    }

    @PatchMapping("/{projectId}")
    public PatchProjectResponse patchProject(
            @AuthenticationPrincipal Account account,
            @RequestBody @Validated PatchProjectRequest patchProjectRequest,
            @PathVariable Long projectId
    ) {
        ProjectUpdateCondition projectUpdateCondition = ProjectUpdateCondition.builder()
                .name(patchProjectRequest.getName())
                .description(patchProjectRequest.getDescription())
                .build();

        projectService.update(account, projectId, projectUpdateCondition);

        return new PatchProjectResponse(projectId);
    }

    @GetMapping("/my/captain")
    public GetMyCaptainProjectResponse getMyCaptainProject(@AuthenticationPrincipal Account account) {
        List<GetMyCaptainProjectResponse.ProjectResponse> projectResponses = projectService.findAllByCaptain(account).stream()
                .map(GetMyCaptainProjectResponse.ProjectResponse::new)
                .collect(Collectors.toList());

        return new GetMyCaptainProjectResponse(projectResponses);
    }

    @GetMapping("/{projectId}/crews")
    public ResponseEntity<GetCrewsResponse> getCrews(@PathVariable Long projectId) {

        List<CrewResponse> crewResponses = projectService.findAllCrews(projectId).stream()
                .map(c -> new CrewResponse(c.getUsername(), c.getNickname()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new GetCrewsResponse(crewResponses));
    }

    @PutMapping("/{projectId}/captain")
    public ResponseEntity<IdDto> updateCaptain(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody UpdateCaptainRequest updateCaptainRequest) {

        Account newCaptain = projectService.updateCaptain(account, updateCaptainRequest.getNewCaptainUsername(), projectId);

        return ResponseEntity.ok(new IdDto(newCaptain.getId()));
    }
}
