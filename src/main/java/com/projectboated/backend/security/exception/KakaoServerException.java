package com.projectboated.backend.security.exception;

import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.common.exception.ErrorCodeException;
import org.springframework.security.core.AuthenticationException;

public class KakaoServerException extends AuthenticationException implements ErrorCodeException {
    private final ErrorCode errorCode;

    public KakaoServerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
