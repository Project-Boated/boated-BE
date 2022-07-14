package com.projectboated.backend.domain.kanban.kanbanlane.entity.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class TaskOriginalIndexOutOfBoundsException extends BusinessException {
    public TaskOriginalIndexOutOfBoundsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
