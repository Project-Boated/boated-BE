package com.projectboated.backend.project.project.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class OnlyCaptainException extends BusinessException {
    public OnlyCaptainException() {
        super(ErrorCode.PROJECT_ONLY_CAPTAIN);
    }
}
