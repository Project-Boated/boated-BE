package com.projectboated.backend.domain.kanban.kanban.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanNotFoundException extends BusinessException {

    public KanbanNotFoundException() {
        super(ErrorCode.KANBAN_NOT_FOUND);
    }
}
