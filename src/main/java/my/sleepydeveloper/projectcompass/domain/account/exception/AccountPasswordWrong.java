package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountPasswordWrong extends BusinessException {
    public AccountPasswordWrong(ErrorCode errorCode) {
        super(errorCode);
    }
}
