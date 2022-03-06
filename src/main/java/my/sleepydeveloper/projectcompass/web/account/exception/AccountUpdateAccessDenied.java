package my.sleepydeveloper.projectcompass.web.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountUpdateAccessDenied extends BaseBusinessException {
    public AccountUpdateAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
