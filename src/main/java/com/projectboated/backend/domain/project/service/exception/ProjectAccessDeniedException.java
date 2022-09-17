package com.projectboated.backend.domain.project.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class ProjectAccessDeniedException extends BusinessException {
    public ProjectAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
