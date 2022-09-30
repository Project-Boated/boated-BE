package com.projectboated.backend.kanban.kanban.controller.response;

import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.controller.dto.common.KanbanLaneResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class GetKanbanResponse {

    private List<KanbanLaneResponse> lanes;

    public GetKanbanResponse(Kanban kanban, List<KanbanLaneResponse> kanbanLaneResponse) {
        this.lanes = kanbanLaneResponse;
    }
}