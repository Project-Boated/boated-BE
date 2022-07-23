package com.projectboated.backend.domain.project.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectDeleteCrewAccessDeniedException extends BusinessException {
    public ProjectDeleteCrewAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
