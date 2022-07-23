package com.projectboated.backend.domain.kanban.kanbanlane.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanLaneNotInProjectException extends BusinessException {
    public KanbanLaneNotInProjectException(ErrorCode errorCode) {
        super(errorCode);
    }
}
