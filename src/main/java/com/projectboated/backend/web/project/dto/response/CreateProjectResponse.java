package com.projectboated.backend.web.project.dto.response;

import lombok.*;
import com.projectboated.backend.domain.project.entity.Project;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectResponse {

    private Long id;

    public CreateProjectResponse(Project project) {
        this.id = project.getId();
    }
}
