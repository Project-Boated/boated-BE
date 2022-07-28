package com.projectboated.backend.domain.kanban.kanbanlane.service.dto;

import lombok.Builder;

@Builder
public record ChangeTaskOrderRequest(
        long projectId,
		
        Long originalLaneId,
        int originalTaskIndex,
		
        Long changeLaneId,
        int changeTaskIndex
		
) {
}
