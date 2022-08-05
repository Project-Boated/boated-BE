package com.projectboated.backend.web.kanban.kanban.dto.response;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.web.kanban.kanbanlane.dto.common.KanbanLaneResponse;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class GetKanbanResponse {

    private List<KanbanLaneResponse> lanes;

    public GetKanbanResponse(List<KanbanLane> lanes, Map<Task, Boolean> taskLikeMap) {
        this.lanes = lanes.stream().map((kl) -> new KanbanLaneResponse(kl, taskLikeMap)).toList();
    }
}