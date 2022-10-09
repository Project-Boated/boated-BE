package com.projectboated.backend.project.projectvideo.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class PutDescriptionRequest {

    @NotBlank
    @Size(max = 255)
    private String description;

    public PutDescriptionRequest(String description) {
        this.description = description;
    }
}
