package com.projectboated.backend.security.kakao.filter.exception;

import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.common.exception.ErrorCodeException;
import org.springframework.security.core.AuthenticationException;

public class KakaoAuthenticationException extends AuthenticationException implements ErrorCodeException {
    public KakaoAuthenticationException() {
        super(ErrorCode.LOGIN_KAKAO_AUTHENTICATION_EXCEPTION.getMessage());
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.LOGIN_KAKAO_AUTHENTICATION_EXCEPTION;
    }
}
