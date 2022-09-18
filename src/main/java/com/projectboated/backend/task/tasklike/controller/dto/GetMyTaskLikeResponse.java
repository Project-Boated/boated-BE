package com.projectboated.backend.task.tasklike.controller.dto;

import com.projectboated.backend.task.tasklike.controller.dto.common.TaskLikeResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class GetMyTaskLikeResponse {

    private List<TaskLikeResponse> taskLikes;

    public GetMyTaskLikeResponse(List<TaskLikeResponse> taskLikes) {
        this.taskLikes = taskLikes;
    }
}
