package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.profileimage.service.ProfileImageService;
import com.projectboated.backend.domain.project.service.ProjectCrewService;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.web.project.dto.response.CrewResponse;
import com.projectboated.backend.web.project.dto.response.GetCrewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/crews")
public class ProjectCrewController {

    private final ProjectCrewService projectCrewService;
    private final ProfileImageService profileImageService;

    @GetMapping
    public GetCrewsResponse getCrews(HttpServletRequest httpRequest,
                                     @RequestParam(required = false) boolean isProxy,
                                     @AuthenticationPrincipal Account account,
                                     @PathVariable Long projectId) {
        List<CrewResponse> crewResponses = projectCrewService.findAllCrews(account.getId(), projectId).stream()
                .map(c -> new CrewResponse(c, profileImageService.getProfileUrl(c.getId(), httpRequest.getHeader("HOST"), isProxy)))
                .toList();

        return new GetCrewsResponse(crewResponses);
    }

    @DeleteMapping("/{crewNickname}")
    public void deleteCrew(@AuthenticationPrincipal Account account,
                           @PathVariable Long projectId,
                           @PathVariable String crewNickname) {
        projectCrewService.deleteCrew(account.getId(), projectId, crewNickname);
    }
}
