package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class WrongPasswordException extends DefaultBusinessException {
    public WrongPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
