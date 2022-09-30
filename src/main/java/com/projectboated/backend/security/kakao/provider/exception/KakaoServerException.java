package com.projectboated.backend.security.kakao.provider.exception;

import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.common.exception.ErrorCodeException;
import org.springframework.security.core.AuthenticationException;

public class KakaoServerException extends AuthenticationException implements ErrorCodeException {
    private final ErrorCode errorCode = ErrorCode.KAKAO_SERVER_EXCEPTION;

    public KakaoServerException() {
        super(ErrorCode.KAKAO_SERVER_EXCEPTION.getMessage());
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
