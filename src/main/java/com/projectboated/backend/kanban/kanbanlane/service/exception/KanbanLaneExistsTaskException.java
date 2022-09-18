package com.projectboated.backend.kanban.kanbanlane.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class KanbanLaneExistsTaskException extends BusinessException {
    public KanbanLaneExistsTaskException() {
        super(ErrorCode.KANBAN_LANE_EXISTS_TASK);
    }
}
