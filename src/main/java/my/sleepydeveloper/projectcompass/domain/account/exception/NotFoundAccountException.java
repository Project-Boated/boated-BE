package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.common.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;

public class NotFoundAccountException extends BaseBusinessException {
    public NotFoundAccountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
