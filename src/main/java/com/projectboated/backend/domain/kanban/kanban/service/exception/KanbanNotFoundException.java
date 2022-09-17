package com.projectboated.backend.domain.kanban.kanban.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class KanbanNotFoundException extends BusinessException {

    public KanbanNotFoundException() {
        super(ErrorCode.KANBAN_NOT_FOUND);
    }
}
