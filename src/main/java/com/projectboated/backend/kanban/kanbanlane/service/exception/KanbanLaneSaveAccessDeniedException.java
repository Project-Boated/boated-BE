package com.projectboated.backend.kanban.kanbanlane.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class KanbanLaneSaveAccessDeniedException extends BusinessException {

    public KanbanLaneSaveAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
