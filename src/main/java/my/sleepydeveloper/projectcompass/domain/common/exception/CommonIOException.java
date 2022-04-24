package my.sleepydeveloper.projectcompass.domain.common.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class CommonIOException extends BusinessException {
    public CommonIOException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
