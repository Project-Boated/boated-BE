package com.projectboated.backend.kanban.kanbanlane.service.dto;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KanbanLaneUpdateRequest {
    private final String name;
    private final Integer order;
    private final Project project;
    private final Kanban kanban;

    @Builder
    public KanbanLaneUpdateRequest(String name, Integer order, Project project, Kanban kanban) {
        this.name = name;
        this.order = order;
        this.project = project;
        this.kanban = kanban;
    }
}
