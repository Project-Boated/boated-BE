package com.projectboated.backend.project.project.controller;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.profileimage.service.ProfileImageService;
import com.projectboated.backend.common.utils.HttpUtils;
import com.projectboated.backend.project.project.controller.dto.response.CrewResponse;
import com.projectboated.backend.project.project.controller.dto.response.GetCrewsResponse;
import com.projectboated.backend.project.project.service.ProjectCrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/crews")
public class ProjectCrewController {

    private final HttpUtils httpUtils;
    private final ProjectCrewService projectCrewService;
    private final ProfileImageService profileImageService;

    @GetMapping
    public GetCrewsResponse getCrews(HttpServletRequest httpRequest,
                                     @RequestParam(required = false) boolean isProxy,
                                     @AuthenticationPrincipal Account account,
                                     @PathVariable Long projectId) {
        List<CrewResponse> crewResponses = projectCrewService.findAllCrews(projectId).stream()
                .map(c -> new CrewResponse(c, profileImageService.getProfileUrl(c.getId(), httpUtils.getHostUrl(httpRequest, isProxy))))
                .toList();

        return new GetCrewsResponse(crewResponses);
    }

    @DeleteMapping("/{crewNickname}")
    public void deleteCrew(@PathVariable Long projectId,
                           @PathVariable String crewNickname) {
        projectCrewService.deleteCrew(projectId, crewNickname);
    }
}
