package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.common.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;

public class ProjectUpdateAccessDenied extends BaseBusinessException {
    public ProjectUpdateAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
