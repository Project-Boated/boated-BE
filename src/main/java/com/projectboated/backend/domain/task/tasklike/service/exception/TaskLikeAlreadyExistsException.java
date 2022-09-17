package com.projectboated.backend.domain.task.tasklike.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class TaskLikeAlreadyExistsException extends BusinessException {
    public TaskLikeAlreadyExistsException() {
        super(ErrorCode.TASK_LIKE_ALREADY_EXISTS);
    }
}
