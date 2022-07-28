package com.projectboated.backend.domain.kanban.kanbanlane.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanLaneNotFoundException extends BusinessException {
    
    public KanbanLaneNotFoundException() {
        super(ErrorCode.KANBAN_LANE_NOT_FOUND);
    }
    
    public KanbanLaneNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
