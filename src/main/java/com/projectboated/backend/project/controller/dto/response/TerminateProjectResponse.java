package com.projectboated.backend.project.controller.dto.response;

import lombok.Getter;

@Getter
public class TerminateProjectResponse {

    private Long id;

    public TerminateProjectResponse(Long projectId) {
        this.id = projectId;
    }
}
