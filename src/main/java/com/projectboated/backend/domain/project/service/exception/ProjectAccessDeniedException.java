package com.projectboated.backend.domain.project.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectAccessDeniedException extends BusinessException {
    public ProjectAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class ProjectFindCrewsAccessDeniedException extends BusinessException {
        public ProjectFindCrewsAccessDeniedException(ErrorCode errorCode) {
            super(errorCode);
        }
    }
}
