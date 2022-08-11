package com.projectboated.backend.domain.kanban.kanbanlane.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanLaneExistsTaskException extends BusinessException {
    public KanbanLaneExistsTaskException() {
        super(ErrorCode.KANBAN_LANE_EXISTS_TASK);
    }
}
