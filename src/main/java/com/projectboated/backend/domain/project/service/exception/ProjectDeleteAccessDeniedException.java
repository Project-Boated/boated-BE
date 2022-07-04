package com.projectboated.backend.domain.project.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectDeleteAccessDeniedException extends BusinessException {

    public ProjectDeleteAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
