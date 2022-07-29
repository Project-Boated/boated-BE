package com.projectboated.backend.domain.project.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectNotFoundException extends BusinessException {

    public ProjectNotFoundException() {
        super(ErrorCode.PROJECT_NOT_FOUND);
    }

    public ProjectNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
