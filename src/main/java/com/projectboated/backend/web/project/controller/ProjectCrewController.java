package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.profileimage.service.AccountProfileImageService;
import com.projectboated.backend.web.project.dto.response.CrewResponse;
import com.projectboated.backend.web.project.dto.response.GetCrewsResponse;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.project.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectCrewController {

    private final ProjectService projectService;
    private final AccountProfileImageService accountProfileImageService;

    @GetMapping("/api/projects/{projectId}/crews")
    public ResponseEntity<GetCrewsResponse> getCrews(HttpServletRequest request,
                                                     @RequestParam(required = false) boolean isProxy,
                                                     @AuthenticationPrincipal Account account,
                                                     @PathVariable Long projectId) {
        List<CrewResponse> crewResponses = projectService.findAllCrews(account, projectId).stream()
                .map(c -> new CrewResponse(c.getId(), c.getUsername(), c.getNickname(), accountProfileImageService.getProfileUrl(c, request, isProxy)))
                .toList();

        return ResponseEntity.ok(new GetCrewsResponse(crewResponses));
    }

    @DeleteMapping("/api/projects/{projectId}/crews/{crewNickname}")
    public void deleteCrew(@AuthenticationPrincipal Account account,
                           @PathVariable Long projectId,
                           @PathVariable String crewNickname) {
        projectService.deleteCrew(account, projectId, crewNickname);
    }
}
