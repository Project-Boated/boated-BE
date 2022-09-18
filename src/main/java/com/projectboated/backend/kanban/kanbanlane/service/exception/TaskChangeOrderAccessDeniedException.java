package com.projectboated.backend.kanban.kanbanlane.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class TaskChangeOrderAccessDeniedException extends BusinessException {
    public TaskChangeOrderAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
