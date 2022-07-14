package com.projectboated.backend.domain.kanban.kanban.entity.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanLaneChangeIndexOutOfBoundsException extends BusinessException {
    public KanbanLaneChangeIndexOutOfBoundsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
