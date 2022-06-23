package org.projectboated.backend.domain.project.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class UpdateCaptainAccessDenied extends BusinessException {

    public UpdateCaptainAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
