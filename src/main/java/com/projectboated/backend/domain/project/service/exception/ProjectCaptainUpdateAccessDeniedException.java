package com.projectboated.backend.domain.project.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectCaptainUpdateAccessDeniedException extends BusinessException {

    public ProjectCaptainUpdateAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
