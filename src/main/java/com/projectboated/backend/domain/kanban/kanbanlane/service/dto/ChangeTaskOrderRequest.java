package com.projectboated.backend.domain.kanban.kanbanlane.service.dto;

import lombok.Builder;

@Builder
public record ChangeTaskOrderRequest(
		
        Long originalLaneId,
        int originalTaskIndex,
		
        Long changeLaneId,
        int changeTaskIndex
		
) {
}
