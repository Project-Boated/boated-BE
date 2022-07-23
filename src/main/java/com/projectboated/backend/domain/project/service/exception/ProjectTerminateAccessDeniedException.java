package com.projectboated.backend.domain.project.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectTerminateAccessDeniedException extends BusinessException {
    public ProjectTerminateAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
