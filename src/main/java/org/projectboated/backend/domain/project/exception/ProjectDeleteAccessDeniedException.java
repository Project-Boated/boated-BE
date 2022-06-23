package org.projectboated.backend.domain.project.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class ProjectDeleteAccessDeniedException extends BusinessException {

    public ProjectDeleteAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
