package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountNicknameDuplicateException extends DefaultBusinessException {
    public AccountNicknameDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
