package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountPasswordWrong extends DefaultBusinessException {
    public AccountPasswordWrong(ErrorCode errorCode) {
        super(errorCode);
    }
}
