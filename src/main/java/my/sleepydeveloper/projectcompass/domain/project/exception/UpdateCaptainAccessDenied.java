package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class UpdateCaptainAccessDenied extends BusinessException {

    public UpdateCaptainAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
