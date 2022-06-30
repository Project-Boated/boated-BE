package com.projectboated.backend.domain.project.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectNotFoundException extends BusinessException {
    public ProjectNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
