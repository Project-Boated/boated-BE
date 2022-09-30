package com.projectboated.backend.project.project.controller.dto.response;

import lombok.Getter;

@Getter
public class CancelTerminateProjectResponse {

    private Long id;

    public CancelTerminateProjectResponse(Long id) {
        this.id = id;
    }
}
