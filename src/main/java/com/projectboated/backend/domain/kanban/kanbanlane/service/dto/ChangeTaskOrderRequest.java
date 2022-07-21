package com.projectboated.backend.domain.kanban.kanbanlane.service.dto;

import lombok.Builder;

@Builder
public record ChangeTaskOrderRequest(
        long projectId,
        int originalLaneIndex,
        int originalTaskIndex,
        int changeLaneIndex,
        int changeTaskIndex
) {
}
