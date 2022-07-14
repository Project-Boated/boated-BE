package com.projectboated.backend.domain.kanban.kanbanlane.entity.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class TaskChangeIndexOutOfBoundsException extends BusinessException {
    public TaskChangeIndexOutOfBoundsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
