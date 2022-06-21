package my.sleepydeveloper.projectcompass.domain.project.dto;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class ProjectFindCrewsAccessDeniedException extends BusinessException {
    public ProjectFindCrewsAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
