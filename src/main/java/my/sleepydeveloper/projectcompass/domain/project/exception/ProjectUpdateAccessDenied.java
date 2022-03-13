package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class ProjectUpdateAccessDenied extends BaseBusinessException {
    public ProjectUpdateAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
