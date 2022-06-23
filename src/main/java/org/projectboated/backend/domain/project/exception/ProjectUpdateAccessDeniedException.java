package org.projectboated.backend.domain.project.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class ProjectUpdateAccessDeniedException extends BusinessException {
    public ProjectUpdateAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
