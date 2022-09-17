package com.projectboated.backend.domain.kanban.kanban.entity.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class KanbanLaneOriginalIndexOutOfBoundsException extends BusinessException {
    public KanbanLaneOriginalIndexOutOfBoundsException() {
        super(ErrorCode.KANBAN_LANE_ORIGINAL_INDEX_OUT_OF_BOUNDS);
    }
}
