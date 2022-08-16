package com.projectboated.backend.web.task.tasklike.dto;

import com.projectboated.backend.web.task.tasklike.dto.common.TaskLikeResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class GetMyTaskLikeResponse {

    private List<TaskLikeResponse> content;

    public GetMyTaskLikeResponse(List<TaskLikeResponse> content) {
        this.content = content;
    }
}
