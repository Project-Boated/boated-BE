package com.projectboated.backend.task.tasklike.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class TaskLikeChangeIndexOutOfBoundsException extends BusinessException {
    public TaskLikeChangeIndexOutOfBoundsException() {
        super(ErrorCode.TASK_LIKE_CHNAGE_INDEX_OUT_OF_BOUNDS);
    }
}
