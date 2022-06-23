package org.projectboated.backend.security.exception;

public class IllegalRequestJsonLogin extends AuthenticationJsonException{
    public IllegalRequestJsonLogin(String message) {
        super(message);
    }
}
