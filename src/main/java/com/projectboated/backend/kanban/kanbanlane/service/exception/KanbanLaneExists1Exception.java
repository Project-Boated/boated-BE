package com.projectboated.backend.kanban.kanbanlane.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class KanbanLaneExists1Exception extends BusinessException {
    public KanbanLaneExists1Exception() {
        super(ErrorCode.KANBAN_LANE_EXISTS_LOWER_1);
    }
}
