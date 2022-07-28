package com.projectboated.backend.web.kanban.kanbanlane.dto.common;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.web.task.task.dto.common.TaskResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class KanbanLaneResponse {

    private Long id;
    private String name;
    private List<TaskResponse> tasks;

    public KanbanLaneResponse(KanbanLane kl) {
        this.id = kl.getId();
        this.name = kl.getName();
        this.tasks = kl.getTasks().stream().map(t -> new TaskResponse(t)).toList();
    }
}