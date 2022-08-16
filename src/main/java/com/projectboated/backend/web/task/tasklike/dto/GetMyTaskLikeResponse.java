package com.projectboated.backend.web.task.tasklike.dto;

import com.projectboated.backend.web.task.tasklike.dto.common.TaskLikeResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class GetMyTaskLikeResponse {

    private List<TaskLikeResponse> taskLikes;

    public GetMyTaskLikeResponse(List<TaskLikeResponse> taskLikes) {
        this.taskLikes = taskLikes;
    }
}
