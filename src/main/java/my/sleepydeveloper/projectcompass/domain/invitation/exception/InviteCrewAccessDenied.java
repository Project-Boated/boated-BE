package my.sleepydeveloper.projectcompass.domain.invitation.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class InviteCrewAccessDenied extends BusinessException {
    public InviteCrewAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
