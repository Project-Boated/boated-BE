package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.common.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;

public class WrongPasswordException extends BaseBusinessException {
    public WrongPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
