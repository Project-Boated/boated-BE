package com.projectboated.backend.web.project.dto.response;

import lombok.Getter;

@Getter
public class PatchProjectResponse {

    private Long id;

    public PatchProjectResponse(Long id) {
        this.id = id;
    }
}
