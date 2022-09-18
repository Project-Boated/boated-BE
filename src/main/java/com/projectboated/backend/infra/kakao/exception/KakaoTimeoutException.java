package com.projectboated.backend.infra.kakao.exception;

import org.springframework.security.core.AuthenticationException;

public class KakaoTimeoutException extends AuthenticationException {
    public KakaoTimeoutException(String msg) {
        super(msg);
    }
}
