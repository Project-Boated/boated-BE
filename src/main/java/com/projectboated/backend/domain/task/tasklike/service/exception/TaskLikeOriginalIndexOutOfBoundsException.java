package com.projectboated.backend.domain.task.tasklike.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class TaskLikeOriginalIndexOutOfBoundsException extends BusinessException {
    public TaskLikeOriginalIndexOutOfBoundsException() {
        super(ErrorCode.TASK_LIKE_ORIGINAL_INDEX_OUT_OF_BOUNDS);
    }
}
