package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.domain.account.account.entity.Account;
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

    private final ProjectTerminateService terminateProject;

    @PostMapping("/terminate")
    public TerminateProjectResponse terminateProject(@AuthenticationPrincipal Account account,
                                                     @PathVariable Long projectId) {
        terminateProject.terminateProject(account.getId(), projectId);
        return new TerminateProjectResponse(projectId);
    }

    @PostMapping("/cancel-terminate")
    public CancelTerminateProjectResponse cancelTerminateProject(@AuthenticationPrincipal Account account,
                                                                 @PathVariable Long projectId) {
        terminateProject.cancelTerminateProject(account.getId(), projectId);
        return new CancelTerminateProjectResponse(projectId);
    }

}
