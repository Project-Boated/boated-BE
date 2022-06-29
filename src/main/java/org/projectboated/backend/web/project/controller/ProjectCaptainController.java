package org.projectboated.backend.web.project.controller;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.project.service.ProjectService;
import org.projectboated.backend.security.dto.IdDto;
import org.projectboated.backend.web.project.dto.UpdateCaptainRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectCaptainController {

    private final ProjectService projectService;

    @PutMapping(value = "/api/projects/{projectId}/captain", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdDto> updateCaptain(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody UpdateCaptainRequest updateCaptainRequest) {

        Account newCaptain = projectService.updateCaptain(account, updateCaptainRequest.getNewCaptainUsername(), projectId);

        return ResponseEntity.ok(new IdDto(newCaptain.getId()));
    }

}
