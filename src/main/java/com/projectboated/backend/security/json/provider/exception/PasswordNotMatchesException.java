package com.projectboated.backend.security.json.provider.exception;

import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.common.exception.ErrorCodeException;
import org.springframework.security.core.AuthenticationException;


public class PasswordNotMatchesException extends AuthenticationException implements ErrorCodeException {
    public PasswordNotMatchesException() {
        super(ErrorCode.LOGIN_PASSWORD_NOT_MATCHES.getMessage());
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.LOGIN_PASSWORD_NOT_MATCHES;
    }
}
