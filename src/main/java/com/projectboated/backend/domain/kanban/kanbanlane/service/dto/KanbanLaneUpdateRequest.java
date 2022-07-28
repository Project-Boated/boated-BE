package com.projectboated.backend.domain.kanban.kanbanlane.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KanbanLaneUpdateRequest {

    private final Long projectId;
    private final Long kanbanLaneId;
    private final String name;

    @Builder
    public KanbanLaneUpdateRequest(Long projectId, Long kanbanLaneId, String name) {
        this.projectId = projectId;
        this.kanbanLaneId = kanbanLaneId;
        this.name = name;
    }
}
