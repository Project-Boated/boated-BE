package com.projectboated.backend.domain.kanban.kanbanlane.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanLaneExists1Exception extends BusinessException {
    public KanbanLaneExists1Exception() {
        super(ErrorCode.KANBAN_LANE_EXISTS_LOWER_1);
    }
}
