package org.projectboated.backend.domain.kanban.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class KanbanNotFoundException extends BusinessException {

    public KanbanNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
