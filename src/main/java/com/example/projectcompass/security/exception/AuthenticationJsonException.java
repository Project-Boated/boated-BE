package com.example.projectcompass.security.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationJsonException extends AuthenticationException {
    public AuthenticationJsonException(String message) {
        super(message);
    }
}
