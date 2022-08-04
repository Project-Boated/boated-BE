package com.projectboated.backend.domain.project.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class OnlyCaptainException extends BusinessException {
    public OnlyCaptainException() {
        super(ErrorCode.PROJECT_ONLY_CAPTAIN);
    }
}
