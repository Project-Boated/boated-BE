package my.sleepydeveloper.projectcompass.domain.invitation.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class InviteCrewAccessDenied extends BaseBusinessException {
    public InviteCrewAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
