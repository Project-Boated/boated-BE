package com.projectboated.backend.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JsonParsingException extends AuthenticationException {
    public JsonParsingException(String msg) {
        super(msg);
    }
}
