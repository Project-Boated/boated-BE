package com.projectboated.backend.domain.project.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectNameSameInAccountException extends BusinessException {
    public ProjectNameSameInAccountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
