package com.projectboated.backend.project.projectvideo.controller.dto.response;

import lombok.Getter;

@Getter
public class GetDescriptionResponse {

    private String description;

    public GetDescriptionResponse(String description) {
        this.description = description;
    }
}
