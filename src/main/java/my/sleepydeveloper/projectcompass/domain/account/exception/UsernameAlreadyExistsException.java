package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class UsernameAlreadyExistsException extends BaseBusinessException {
    public UsernameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
