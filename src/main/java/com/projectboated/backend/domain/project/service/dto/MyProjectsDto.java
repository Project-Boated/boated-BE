package com.projectboated.backend.domain.project.service.dto;

import lombok.Builder;
import lombok.Getter;
import com.projectboated.backend.domain.project.entity.Project;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyProjectsDto {

    private int page;
    private int size;
    private boolean hasNext;
    private List<Project> projects;

    @Builder
    public MyProjectsDto(int page, int size, boolean hasNext, List<Project> projects) {
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
        this.projects = projects;
    }
}
