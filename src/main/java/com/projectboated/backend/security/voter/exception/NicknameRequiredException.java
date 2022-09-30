package com.projectboated.backend.security.voter.exception;

import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.common.exception.ErrorCodeException;
import org.springframework.security.access.AccessDeniedException;

public class NicknameRequiredException extends AccessDeniedException implements ErrorCodeException {

    public NicknameRequiredException() {
        super(ErrorCode.ACCOUNT_NICKNAME_REQUIRED.getMessage());
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ACCOUNT_NICKNAME_REQUIRED;
    }
}
