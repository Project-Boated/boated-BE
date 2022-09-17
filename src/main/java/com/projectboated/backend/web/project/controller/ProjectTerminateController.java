package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.project.service.ProjectTerminateService;
import com.projectboated.backend.web.project.dto.response.CancelTerminateProjectResponse;
import com.projectboated.backend.web.project.dto.response.TerminateProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}")
public class ProjectTerminateController {

    private final ProjectTerminateService terminateProjectService
            ;

    @PostMapping("/terminate")
    public TerminateProjectResponse terminateProject(@PathVariable Long projectId) {
        terminateProjectService.terminateProject(projectId);
        return new TerminateProjectResponse(projectId);
    }

    @PostMapping("/cancel-terminate")
    public CancelTerminateProjectResponse cancelTerminateProject(@AuthenticationPrincipal Account account,
                                                                 @PathVariable Long projectId) {
        terminateProjectService.cancelTerminateProject(projectId);
        return new CancelTerminateProjectResponse(projectId);
    }

}
