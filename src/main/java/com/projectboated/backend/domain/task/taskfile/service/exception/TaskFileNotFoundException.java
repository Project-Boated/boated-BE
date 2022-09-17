package com.projectboated.backend.domain.task.taskfile.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class TaskFileNotFoundException extends BusinessException {
    public TaskFileNotFoundException() {
        super(ErrorCode.TASK_FILE_NOT_FOUND);
    }
}
