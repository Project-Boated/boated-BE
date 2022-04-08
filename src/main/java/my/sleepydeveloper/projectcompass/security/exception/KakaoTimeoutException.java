package my.sleepydeveloper.projectcompass.security.exception;

import org.springframework.security.core.AuthenticationException;

public class KakaoTimeoutException extends AuthenticationException {
    public KakaoTimeoutException(String msg) {
        super(msg);
    }
}
