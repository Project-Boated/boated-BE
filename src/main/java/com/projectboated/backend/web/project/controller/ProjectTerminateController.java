package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.web.project.dto.response.CancelTerminateProjectResponse;
import com.projectboated.backend.web.project.dto.response.TerminateProjectResponse;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.project.service.ProjectService;
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
