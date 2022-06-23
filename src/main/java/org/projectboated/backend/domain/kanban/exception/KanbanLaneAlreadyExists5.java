package org.projectboated.backend.domain.kanban.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class KanbanLaneAlreadyExists5 extends BusinessException {

    public KanbanLaneAlreadyExists5(ErrorCode errorCode) {
        super(errorCode);
    }
}
