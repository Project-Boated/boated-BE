package org.projectboated.backend.web.project.controller;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.project.service.ProjectService;
import org.projectboated.backend.web.project.dto.CancelTerminateProjectResponse;
import org.projectboated.backend.web.project.dto.TerminateProjectResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectTerminateController {

    private final ProjectService projectService;

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
