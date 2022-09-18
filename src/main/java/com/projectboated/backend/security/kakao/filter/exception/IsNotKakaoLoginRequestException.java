package com.projectboated.backend.security.kakao.filter.exception;

import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.common.exception.ErrorCodeException;
import org.springframework.security.core.AuthenticationException;

public class IsNotKakaoLoginRequestException extends AuthenticationException implements ErrorCodeException {
    public IsNotKakaoLoginRequestException() {
        super(ErrorCode.LOGIN_IS_NOT_KAKAO_REQUEST.getMessage());
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.LOGIN_IS_NOT_KAKAO_REQUEST;
    }
}
