package my.sleepydeveloper.projectcompass.web.project.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.accountproject.service.AccountProjectService;
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
public class CrewController {

    private final ProjectService projectService;

    @GetMapping("/api/projects/{projectId}/crews")
    public ResponseEntity<GetCrewsResponse> getCrews(@PathVariable Long projectId) {

        List<CrewResponse> crewResponses = projectService.findAllCrews(projectId).stream()
                .map(c -> new CrewResponse(c.getUsername(), c.getNickname()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new GetCrewsResponse(crewResponses));
    }

    @DeleteMapping("/api/projects/{projectId}/crews/{crewNickname}")
    public void deleteCrew(@AuthenticationPrincipal Account account,
                           @PathVariable Long projectId,
                           @PathVariable String crewNickname) {
        projectService.deleteCrew(account, projectId, crewNickname);
    }
}
