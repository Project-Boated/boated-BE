package com.projectboated.backend.kanban.kanbanlane.controller.dto.common;

import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.task.task.controller.dto.common.TaskResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class KanbanLaneResponse {

    private Long id;
    private String name;
    private List<TaskResponse> tasks;

    public KanbanLaneResponse(KanbanLane kanbanLane, List<TaskResponse> tasks) {
        this.id = kanbanLane.getId();
        this.name = kanbanLane.getName();
        this.tasks = tasks;
    }
}