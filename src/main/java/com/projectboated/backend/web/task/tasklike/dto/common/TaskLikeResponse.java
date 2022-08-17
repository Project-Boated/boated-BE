package com.projectboated.backend.web.task.tasklike.dto.common;

import com.projectboated.backend.web.project.dto.common.ProjectNoAccountsResponse;
import com.projectboated.backend.web.task.task.dto.common.TaskNoLikeResponse;
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
