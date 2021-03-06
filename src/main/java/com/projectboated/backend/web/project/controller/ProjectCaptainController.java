package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.service.ProjectCaptainService;
import com.projectboated.backend.web.project.dto.request.UpdateProjectCaptainRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectCaptainController {

    private final ProjectCaptainService projectCaptainService;

    @PutMapping(value = "/api/projects/{projectId}/captain", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateProjectCaptain(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody UpdateProjectCaptainRequest params) {
        projectCaptainService.updateCaptain(account.getId(), projectId, params.getNickname());
    }

}
