package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class ProjectUpdateAccessDenied extends DefaultBusinessException {
    public ProjectUpdateAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
