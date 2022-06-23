package org.projectboated.backend.domain.project.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class ProjectAccessDeniedException extends BusinessException {
    public ProjectAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}