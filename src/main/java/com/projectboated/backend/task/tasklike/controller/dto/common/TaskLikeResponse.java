package com.projectboated.backend.task.tasklike.controller.dto.common;

import com.projectboated.backend.project.project.controller.dto.common.ProjectNoAccountsResponse;
import com.projectboated.backend.task.task.controller.dto.common.TaskNoLikeResponse;
import lombok.Getter;

@Getter
public class TaskLikeResponse {

    private ProjectNoAccountsResponse project;
    private TaskNoLikeResponse task;

    public TaskLikeResponse(ProjectNoAccountsResponse projectResponse, TaskNoLikeResponse taskNoLikeResponse) {
        this.project = projectResponse;
        this.task = taskNoLikeResponse;
    }
}
