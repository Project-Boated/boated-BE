package com.projectboated.backend.security.exception;

import org.springframework.security.core.AuthenticationException;

public class IsNotKakaoLoginRequest extends AuthenticationException {
    public IsNotKakaoLoginRequest(String msg) {
        super(msg);
    }
}
