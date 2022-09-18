package com.projectboated.backend.task.tasklike.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class TaskLikeNotFoundException extends BusinessException {
    public TaskLikeNotFoundException() {
        super(ErrorCode.TASK_LIKE_NOT_FOUND);
    }
}
