package com.projectboated.backend.web.project.dto.response;

import lombok.*;
import com.projectboated.backend.domain.project.entity.Project;

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
