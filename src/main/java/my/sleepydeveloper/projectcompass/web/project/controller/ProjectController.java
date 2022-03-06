package my.sleepydeveloper.projectcompass.web.project.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.invitation.Invitation;
import my.sleepydeveloper.projectcompass.domain.invitation.InvitationService;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.service.ProjectService;
import my.sleepydeveloper.projectcompass.domain.project.vo.ProjectUpdateCondition;
import my.sleepydeveloper.projectcompass.security.dto.IdDto;
import my.sleepydeveloper.projectcompass.web.project.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<IdDto> save(
            @AuthenticationPrincipal Account account,
            @RequestBody @Validated ProjectSaveRequest projectSaveRequest) {

        Project project = projectService.save(new Project(projectSaveRequest.getName(), projectSaveRequest.getDescription(), account));

        return ResponseEntity.ok(new IdDto(project.getId()));
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<IdDto> update(
            @AuthenticationPrincipal Account account,
            @RequestBody @Validated ProjectUpdateRequest projectUpdateRequest,
            @PathVariable Long projectId
    ) {
        ProjectUpdateCondition projectUpdateCondition = ProjectUpdateCondition.builder()
                .name(projectUpdateRequest.getName())
                .description(projectUpdateRequest.getDescription())
                .build();

        projectService.update(account, projectId, projectUpdateCondition);

        return ResponseEntity.ok(new IdDto(projectId));
    }

    @GetMapping("/my")
    public ResponseEntity<MyProjectResponse> myProject(@AuthenticationPrincipal Account account) {
        List<ProjectResponse> projectResponses = projectService.findAllByAccount(account).stream()
                .map(p -> new ProjectResponse(p.getName(), p.getDescription()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new MyProjectResponse(projectResponses));
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

    @PostMapping("/{projectId}/crews")
    public ResponseEntity<IdDto> inviteCrew(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody InviteCrewRequest inviteCrewRequest
    ) {
        Invitation invitation = invitationService.inviteCrew(account, inviteCrewRequest.getUsername(), projectId);
        return ResponseEntity.ok(new IdDto(invitation.getId()));
    }
}
