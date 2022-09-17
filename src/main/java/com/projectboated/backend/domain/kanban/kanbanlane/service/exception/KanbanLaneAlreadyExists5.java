package com.projectboated.backend.domain.kanban.kanbanlane.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class KanbanLaneAlreadyExists5 extends BusinessException {

    public KanbanLaneAlreadyExists5() {
        super(ErrorCode.KANBAN_LANE_EXISTS_UPPER_5);
    }
}
