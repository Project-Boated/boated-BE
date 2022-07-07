package com.projectboated.backend.web.project.dto.response;

import lombok.Setter;

@Setter
public class TerminateProjectResponse {

    private Long id;

    public TerminateProjectResponse(Long projectId) {
        this.id = projectId;
    }
}
