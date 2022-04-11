package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class ProjectNotFoundException extends DefaultBusinessException {
    public ProjectNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
