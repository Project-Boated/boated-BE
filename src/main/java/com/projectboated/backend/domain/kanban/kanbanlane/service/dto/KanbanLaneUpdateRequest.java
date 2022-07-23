package com.projectboated.backend.domain.kanban.kanbanlane.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KanbanLaneUpdateRequest {

    private final Long projectId;
    private final int kanbanLaneIndex;
    private final String name;

    @Builder
    public KanbanLaneUpdateRequest(Long projectId, int kanbanLaneIndex, String name) {
        this.projectId = projectId;
        this.kanbanLaneIndex = kanbanLaneIndex;
        this.name = name;
    }
}
