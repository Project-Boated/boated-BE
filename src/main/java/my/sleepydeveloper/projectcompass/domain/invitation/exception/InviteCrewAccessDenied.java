package my.sleepydeveloper.projectcompass.domain.invitation.exception;

import my.sleepydeveloper.projectcompass.common.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;

public class InviteCrewAccessDenied extends BaseBusinessException {
    public InviteCrewAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
