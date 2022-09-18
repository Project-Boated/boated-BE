package com.projectboated.backend.kanban.kanbanlane.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class KanbanLaneUpdateAccessDeniedException extends BusinessException {
    public KanbanLaneUpdateAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
