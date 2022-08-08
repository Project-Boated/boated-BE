package com.projectboated.backend.web.kanban.kanbanlane.dto;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import lombok.Getter;

import java.util.List;

@Getter
public class GetLanesResponse {

    private List<KanbanLaneResponse> lanes;

    public GetLanesResponse(List<KanbanLane> lanes) {
        this.lanes = lanes.stream()
                .map(KanbanLaneResponse::new)
                .toList();
    }

    @Getter
    static class KanbanLaneResponse {
        private Long id;
        private String name;

        public KanbanLaneResponse(KanbanLane kanbanLane) {
            this.id = kanbanLane.getId();
            this.name = kanbanLane.getName();
        }
    }
}
