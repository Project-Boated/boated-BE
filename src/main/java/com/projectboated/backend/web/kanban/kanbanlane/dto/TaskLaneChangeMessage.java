package com.projectboated.backend.web.kanban.kanbanlane.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TaskLaneChangeMessage  {

    private int originalIndex;
    private int changeIndex;

    @Builder
    public TaskLaneChangeMessage(int originalIndex, int changeIndex) {
        this.originalIndex = originalIndex;
        this.changeIndex = changeIndex;
    }
}
