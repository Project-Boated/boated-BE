package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
