package org.projectboated.backend.domain.project.service.dto;

import lombok.Builder;
import lombok.Getter;
import org.projectboated.backend.domain.project.entity.Project;

import java.util.List;

@Builder
@Getter
public class MyProjectsDto {

    private int page;
    private int size;
    private boolean hasNext;
    private List<Project> projects;

}
