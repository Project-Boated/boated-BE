package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class ProjectUpdateAccessDenied extends BusinessException {
    public ProjectUpdateAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
