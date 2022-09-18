package com.projectboated.backend.project.project.controller;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.controller.dto.request.UpdateProjectCaptainRequest;
import com.projectboated.backend.project.project.service.ProjectCaptainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/captain")
public class ProjectCaptainController {

    private final ProjectCaptainService projectCaptainService;

    @PutMapping
    public void updateProjectCaptain(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody UpdateProjectCaptainRequest params) {
        projectCaptainService.updateCaptain(account.getId(), projectId, params.getNickname());
    }

}
