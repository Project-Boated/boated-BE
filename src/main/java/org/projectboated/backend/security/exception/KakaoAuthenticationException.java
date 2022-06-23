package org.projectboated.backend.security.exception;

import org.springframework.security.core.AuthenticationException;

public class KakaoAuthenticationException extends AuthenticationException {
    public KakaoAuthenticationException(String msg) {
        super(msg);
    }
}
