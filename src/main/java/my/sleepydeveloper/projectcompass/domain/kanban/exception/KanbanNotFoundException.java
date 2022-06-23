package my.sleepydeveloper.projectcompass.domain.kanban.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class KanbanNotFoundException extends BusinessException {

    public KanbanNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
