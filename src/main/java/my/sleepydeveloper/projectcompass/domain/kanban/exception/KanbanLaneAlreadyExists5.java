package my.sleepydeveloper.projectcompass.domain.kanban.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class KanbanLaneAlreadyExists5 extends BusinessException {

    public KanbanLaneAlreadyExists5(ErrorCode errorCode) {
        super(errorCode);
    }
}
