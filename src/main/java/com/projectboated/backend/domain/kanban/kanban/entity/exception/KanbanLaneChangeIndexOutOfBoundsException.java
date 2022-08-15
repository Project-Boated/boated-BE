package com.projectboated.backend.domain.kanban.kanban.entity.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class KanbanLaneChangeIndexOutOfBoundsException extends BusinessException {
    public KanbanLaneChangeIndexOutOfBoundsException() {
        super(ErrorCode.KANBAN_LANE_CHANGE_INDEX_OUT_OF_BOUNDS);
    }
}
