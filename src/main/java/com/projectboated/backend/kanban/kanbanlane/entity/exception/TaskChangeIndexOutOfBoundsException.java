package com.projectboated.backend.kanban.kanbanlane.entity.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class TaskChangeIndexOutOfBoundsException extends BusinessException {
    public TaskChangeIndexOutOfBoundsException() {
        super(ErrorCode.TASK_CHANGE_INDEX_OUT_OF_BOUNDS);
    }
}
