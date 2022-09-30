package com.projectboated.backend.task.task.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class TaskNotFoundException extends BusinessException {
    public TaskNotFoundException() {
        super(ErrorCode.TASK_NOT_FOUND);
    }
}
