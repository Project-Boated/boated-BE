package com.projectboated.backend.security.json.filter.exception;

import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.common.exception.ErrorCodeException;

import javax.security.sasl.AuthenticationException;

public class UsernameAndPasswordRequiredException extends AuthenticationException implements ErrorCodeException {
    public UsernameAndPasswordRequiredException() {
        super(ErrorCode.LOGIN_USERNAME_PASSWORD_REQUIRED.getMessage());
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.LOGIN_USERNAME_PASSWORD_REQUIRED;
    }
}
