package my.sleepydeveloper.projectcompass.security.exception;

import org.springframework.security.core.AuthenticationException;

public class IsNotKakaoLoginRequest extends AuthenticationException {
    public IsNotKakaoLoginRequest(String msg) {
        super(msg);
    }
}
