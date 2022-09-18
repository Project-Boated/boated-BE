package com.projectboated.backend.kanban.kanbanlane.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class KanbanLaneChangeIndexDeniedException extends BusinessException {
    public KanbanLaneChangeIndexDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
