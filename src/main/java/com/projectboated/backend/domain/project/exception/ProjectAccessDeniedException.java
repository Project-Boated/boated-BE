package com.projectboated.backend.domain.project.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectAccessDeniedException extends BusinessException {
    public ProjectAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
