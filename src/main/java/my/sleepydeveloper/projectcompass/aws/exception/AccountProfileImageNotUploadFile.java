package my.sleepydeveloper.projectcompass.aws.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountProfileImageNotUploadFile extends BusinessException {
    public AccountProfileImageNotUploadFile(ErrorCode errorCode) {
        super(errorCode);
    }
}
