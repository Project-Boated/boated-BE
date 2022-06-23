package org.projectboated.backend.domain.kanban.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class KanbanLaneNotFoundException extends BusinessException {
    public KanbanLaneNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
