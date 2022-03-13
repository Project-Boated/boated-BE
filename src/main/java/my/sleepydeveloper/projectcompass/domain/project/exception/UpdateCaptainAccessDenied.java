package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class UpdateCaptainAccessDenied extends BaseBusinessException {

    public UpdateCaptainAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
