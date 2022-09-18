package com.projectboated.backend.project.controller.dto.response;

import com.projectboated.backend.project.entity.Project;
import lombok.Getter;

@Getter
public class CreateProjectResponse {

    private Long id;

    public CreateProjectResponse(Project project) {
        this.id = project.getId();
    }

    public CreateProjectResponse(Long id) {
        this.id = id;
    }
}
