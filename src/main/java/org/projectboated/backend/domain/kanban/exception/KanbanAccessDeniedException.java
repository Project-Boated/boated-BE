package org.projectboated.backend.domain.kanban.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class KanbanAccessDeniedException extends BusinessException {
    public KanbanAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
