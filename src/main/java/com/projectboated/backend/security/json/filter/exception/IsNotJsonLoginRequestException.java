package com.projectboated.backend.security.json.filter.exception;

import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.common.exception.ErrorCodeException;
import org.springframework.security.core.AuthenticationException;

public class IsNotJsonLoginRequestException extends AuthenticationException implements ErrorCodeException {

    public IsNotJsonLoginRequestException() {
        super(ErrorCode.LOGIN_IS_NOT_JSON_REQUEST.getMessage());
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.LOGIN_IS_NOT_JSON_REQUEST;
    }
}
