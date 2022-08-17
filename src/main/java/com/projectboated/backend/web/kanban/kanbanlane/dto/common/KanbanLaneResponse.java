package com.projectboated.backend.web.kanban.kanbanlane.dto.common;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.web.task.task.dto.common.TaskResponse;
import lombok.Getter;

import java.util.List;
import java.util.Map;

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