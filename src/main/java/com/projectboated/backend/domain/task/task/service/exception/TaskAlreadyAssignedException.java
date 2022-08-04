package com.projectboated.backend.domain.task.task.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class TaskAlreadyAssignedException extends BusinessException {
    public TaskAlreadyAssignedException() {
        super(ErrorCode.TASK_ALREADY_ASSIGNED);
    }
}
