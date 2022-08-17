package com.projectboated.backend.web.kanban.kanban.dto.response;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.web.kanban.kanbanlane.dto.common.KanbanLaneResponse;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class GetKanbanResponse {

    private List<KanbanLaneResponse> lanes;

    public GetKanbanResponse(Kanban kanban, List<KanbanLaneResponse> kanbanLaneResponse) {
        this.lanes = kanbanLaneResponse;
    }
}