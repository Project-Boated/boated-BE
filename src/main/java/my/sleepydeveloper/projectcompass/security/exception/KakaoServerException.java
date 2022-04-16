package my.sleepydeveloper.projectcompass.security.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class KakaoServerException extends AuthenticationException implements BusinessException {
    private final ErrorCode errorCode;

    public KakaoServerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
