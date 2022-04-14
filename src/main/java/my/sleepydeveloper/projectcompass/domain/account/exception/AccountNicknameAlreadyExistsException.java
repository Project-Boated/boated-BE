package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountNicknameAlreadyExistsException extends DefaultBusinessException {
    public AccountNicknameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
