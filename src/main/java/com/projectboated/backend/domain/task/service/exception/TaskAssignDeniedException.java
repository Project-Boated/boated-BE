package com.projectboated.backend.domain.task.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class TaskAssignDeniedException extends BusinessException {
    public TaskAssignDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
