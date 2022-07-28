package com.projectboated.backend.domain.task.task.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class TaskSaveAccessDeniedException extends BusinessException {
    public TaskSaveAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
