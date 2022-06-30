package com.projectboated.backend.security.exception;

import com.projectboated.backend.domain.common.exception.ErrorCodeException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
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
