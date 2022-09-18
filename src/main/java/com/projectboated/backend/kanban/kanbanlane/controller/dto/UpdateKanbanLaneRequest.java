package com.projectboated.backend.kanban.kanbanlane.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateKanbanLaneRequest {

    private String name;

    @Builder
    public UpdateKanbanLaneRequest(String name) {
        this.name = name;
    }
}
