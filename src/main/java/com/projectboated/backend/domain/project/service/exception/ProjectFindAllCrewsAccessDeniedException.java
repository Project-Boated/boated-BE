package com.projectboated.backend.domain.project.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectFindAllCrewsAccessDeniedException extends BusinessException {
    public ProjectFindAllCrewsAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}