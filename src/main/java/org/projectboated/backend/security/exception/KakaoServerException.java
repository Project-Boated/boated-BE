package org.projectboated.backend.security.exception;

import org.projectboated.backend.domain.exception.ErrorCodeException;
import org.projectboated.backend.domain.exception.ErrorCode;
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
