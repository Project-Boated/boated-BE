package com.projectboated.backend.web.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchProjectResponse {

    private Long id;

    public PatchProjectResponse(Long id) {
        this.id = id;
    }
}
