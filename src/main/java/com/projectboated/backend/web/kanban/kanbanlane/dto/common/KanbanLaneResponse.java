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

    public KanbanLaneResponse(KanbanLane kl, Map<Task, Boolean> taskLikeMap) {
        this.id = kl.getId();
        this.name = kl.getName();
        this.tasks = kl.getTasks().stream().map(t -> new TaskResponse(t, taskLikeMap)).toList();
    }
}