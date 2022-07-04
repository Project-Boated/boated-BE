package com.projectboated.backend.domain.kanban.kanbanlane.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanLaneAlreadyExists5 extends BusinessException {

    public KanbanLaneAlreadyExists5(ErrorCode errorCode) {
        super(errorCode);
    }
}
