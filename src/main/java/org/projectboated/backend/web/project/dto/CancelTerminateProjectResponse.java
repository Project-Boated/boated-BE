package org.projectboated.backend.web.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CancelTerminateProjectResponse {

    private Long id;

    public CancelTerminateProjectResponse(Long id) {
        this.id = id;
    }
}
