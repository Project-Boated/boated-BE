package com.projectboated.backend.web.kanban.kanban.dto.response;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.web.kanban.kanbanlane.dto.common.KanbanLaneResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class GetKanbanResponse {

    private List<KanbanLaneResponse> lanes;

    public GetKanbanResponse(List<KanbanLane> lanes) {
        this.lanes = lanes.stream().map(KanbanLaneResponse::new).toList();
    }
}