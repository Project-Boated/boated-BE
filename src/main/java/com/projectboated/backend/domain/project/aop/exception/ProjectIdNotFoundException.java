package com.projectboated.backend.domain.project.aop.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectIdNotFoundException extends BusinessException {
    public ProjectIdNotFoundException() {
        super(ErrorCode.AOP_PROJECT_ID_NOT_FOUND);
    }
}
