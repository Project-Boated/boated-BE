package org.projectboated.backend.domain.task.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class TaskSaveAccessDeniedException extends BusinessException {
    public TaskSaveAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
