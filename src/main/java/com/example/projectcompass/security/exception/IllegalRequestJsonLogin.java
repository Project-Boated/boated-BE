package com.example.projectcompass.security.exception;

public class IllegalRequestJsonLogin extends AuthenticationJsonException{
    public IllegalRequestJsonLogin(String message) {
        super(message);
    }
}
