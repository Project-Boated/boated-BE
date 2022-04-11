package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class UsernameAlreadyExistsException extends DefaultBusinessException {
    public UsernameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
