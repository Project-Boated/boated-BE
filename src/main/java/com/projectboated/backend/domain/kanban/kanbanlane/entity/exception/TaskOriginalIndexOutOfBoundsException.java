package com.projectboated.backend.domain.kanban.kanbanlane.entity.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class TaskOriginalIndexOutOfBoundsException extends BusinessException {
    public TaskOriginalIndexOutOfBoundsException() {
        super(ErrorCode.TASK_ORIGINAL_INDEX_OUT_OF_BOUNDS);
    }
}
