package com.projectboated.backend.domain.kanban.kanban.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class KanbanAccessDeniedException extends BusinessException {
    public KanbanAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
