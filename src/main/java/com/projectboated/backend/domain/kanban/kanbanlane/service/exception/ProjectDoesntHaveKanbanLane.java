package com.projectboated.backend.domain.kanban.kanbanlane.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class ProjectDoesntHaveKanbanLane extends BusinessException {
    public ProjectDoesntHaveKanbanLane(ErrorCode errorCode) {
        super(errorCode);
    }
}
