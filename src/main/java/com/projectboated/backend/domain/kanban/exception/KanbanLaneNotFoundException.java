package com.projectboated.backend.domain.kanban.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanLaneNotFoundException extends BusinessException {
    public KanbanLaneNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
