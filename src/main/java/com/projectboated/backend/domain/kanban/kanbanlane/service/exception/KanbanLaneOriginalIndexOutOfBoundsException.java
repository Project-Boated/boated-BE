package com.projectboated.backend.domain.kanban.kanbanlane.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanLaneOriginalIndexOutOfBoundsException extends BusinessException {
    public KanbanLaneOriginalIndexOutOfBoundsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
