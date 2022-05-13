package my.sleepydeveloper.projectcompass.web.project.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.accountproject.service.AccountProjectService;
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
    private final AccountProjectService accountProjectService;
    private final InvitationService invitationService;

    @PostMapping
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

    @PatchMapping("/{projectId}")
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

    @GetMapping("/my/captain")
    public GetMyCaptainProjectResponse getMyCaptainProject(@AuthenticationPrincipal Account account) {
        List<GetMyCaptainProjectResponse.ProjectResponse> projects = projectService.findAllByCaptainNotTerminated(account).stream()
                .map((p) -> new GetMyCaptainProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .collect(Collectors.toList());

        return new GetMyCaptainProjectResponse(projects);
    }

    @GetMapping("/my/crew")
    public GetMyCrewProjectResponse getMyCrewProject(@AuthenticationPrincipal Account account) {
        List<GetMyCrewProjectResponse.ProjectResponse> projects = projectService.findAllByCrewNotTerminated(account).stream()
                .map((p) -> new GetMyCrewProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .toList();

        return new GetMyCrewProjectResponse(projects);
    }

    @GetMapping("/my/captain/terminated")
    public GetMyCaptainTerminatedProjectResponse getMyCaptainTerminatedProject(@AuthenticationPrincipal Account account) {
        List<GetMyCaptainTerminatedProjectResponse.ProjectResponse> projects = projectService.findAllByCaptainTerminated(account).stream()
                .map((p) -> new GetMyCaptainTerminatedProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .collect(Collectors.toList());

        return new GetMyCaptainTerminatedProjectResponse(projects);
    }

    @GetMapping("/my/crew/terminated")
    public GetMyCrewTerminatedProjectResponse getMyCrewTerminatedProject(@AuthenticationPrincipal Account account) {
        List<GetMyCrewTerminatedProjectResponse.ProjectResponse> projects = projectService.findAllByCrewTerminated(account).stream()
                .map((p) -> new GetMyCrewTerminatedProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .collect(Collectors.toList());

        return new GetMyCrewTerminatedProjectResponse(projects);
    }

    @GetMapping("/{projectId}/crews")
    public ResponseEntity<GetCrewsResponse> getCrews(@PathVariable Long projectId) {

        List<CrewResponse> crewResponses = projectService.findAllCrews(projectId).stream()
                .map(c -> new CrewResponse(c.getUsername(), c.getNickname()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new GetCrewsResponse(crewResponses));
    }

    @DeleteMapping("/{projectId}/crews/{crewNickname}")
    public void deleteCrew(@AuthenticationPrincipal Account account,
                           @PathVariable Long projectId,
                           @PathVariable String crewNickname) {
        projectService.deleteCrew(account, projectId, crewNickname);
    }

    @PutMapping("/{projectId}/captain")
    public ResponseEntity<IdDto> updateCaptain(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody UpdateCaptainRequest updateCaptainRequest) {

        Account newCaptain = projectService.updateCaptain(account, updateCaptainRequest.getNewCaptainUsername(), projectId);

        return ResponseEntity.ok(new IdDto(newCaptain.getId()));
    }

    @PostMapping("/{projectId}/terminate")
    public TerminateProjectResponse terminateProject(@AuthenticationPrincipal Account account,
                                                     @PathVariable Long projectId) {
        projectService.terminateProject(account, projectId);

        return new TerminateProjectResponse(projectId);
    }

    @PostMapping("/{projectId}/cancel-terminate")
    public CancelTerminateProjectResponse cancelTerminateProject(@AuthenticationPrincipal Account account,
                                                     @PathVariable Long projectId) {
        projectService.cancelTerminateProject(account, projectId);

        return new CancelTerminateProjectResponse(projectId);
    }
}
