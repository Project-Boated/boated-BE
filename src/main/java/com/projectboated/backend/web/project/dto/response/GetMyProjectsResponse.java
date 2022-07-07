package com.projectboated.backend.web.project.dto.response;

import com.projectboated.backend.web.project.dto.common.ProjectResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GetMyProjectsResponse {

    private int page;
    private int size;
    private boolean hasNext;
    private List<ProjectResponse> content;

    public GetMyProjectsResponse(int page, int size, boolean hasNext, List<ProjectResponse> projectResponse) {
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
        this.content = projectResponse;
    }
}
