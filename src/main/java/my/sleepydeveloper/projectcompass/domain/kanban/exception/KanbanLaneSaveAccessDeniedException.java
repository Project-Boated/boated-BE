package my.sleepydeveloper.projectcompass.domain.kanban.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class KanbanLaneSaveAccessDeniedException extends BusinessException {

    public KanbanLaneSaveAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
