package com.projectboated.backend.domain.project.dto;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectFindCrewsAccessDeniedException extends BusinessException {
    public ProjectFindCrewsAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
