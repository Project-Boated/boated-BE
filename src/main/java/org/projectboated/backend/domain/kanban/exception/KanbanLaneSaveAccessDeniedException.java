package org.projectboated.backend.domain.kanban.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class KanbanLaneSaveAccessDeniedException extends BusinessException {

    public KanbanLaneSaveAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
