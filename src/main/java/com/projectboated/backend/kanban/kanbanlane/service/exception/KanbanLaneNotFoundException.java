package com.projectboated.backend.kanban.kanbanlane.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class KanbanLaneNotFoundException extends BusinessException {

    public KanbanLaneNotFoundException() {
        super(ErrorCode.KANBAN_LANE_NOT_FOUND);
    }
}
