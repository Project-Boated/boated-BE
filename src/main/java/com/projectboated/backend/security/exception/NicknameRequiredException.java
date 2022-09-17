package com.projectboated.backend.security.exception;

import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.common.exception.ErrorCodeException;
import org.springframework.security.access.AccessDeniedException;

public class NicknameRequiredException extends AccessDeniedException implements ErrorCodeException {

    private final ErrorCode errorCode;

    public NicknameRequiredException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
