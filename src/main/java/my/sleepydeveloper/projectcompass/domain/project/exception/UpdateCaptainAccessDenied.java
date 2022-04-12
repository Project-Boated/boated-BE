package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class UpdateCaptainAccessDenied extends DefaultBusinessException {

    public UpdateCaptainAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
