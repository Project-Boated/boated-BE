package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountNotFoundException extends DefaultBusinessException {
    public AccountNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
