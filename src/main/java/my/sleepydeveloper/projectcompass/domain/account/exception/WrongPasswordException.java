package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class WrongPasswordException extends BaseBusinessException {
    public WrongPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
