package com.projectboated.backend.domain.kanban.kanbanlane.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanLaneChangeIndexDeniedException extends BusinessException {
    public KanbanLaneChangeIndexDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
