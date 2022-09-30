package com.projectboated.backend.security.common.exception;

import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.common.exception.ErrorCodeException;
import org.springframework.security.core.AuthenticationException;

public class JsonParsingException extends AuthenticationException implements ErrorCodeException {
    public JsonParsingException() {
        super(ErrorCode.JSON_PARSING_EXCEPTION.getMessage());
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.JSON_PARSING_EXCEPTION;
    }
}
