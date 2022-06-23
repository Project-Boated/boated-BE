package org.projectboated.backend.domain.project.dto;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class ProjectFindCrewsAccessDeniedException extends BusinessException {
    public ProjectFindCrewsAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
