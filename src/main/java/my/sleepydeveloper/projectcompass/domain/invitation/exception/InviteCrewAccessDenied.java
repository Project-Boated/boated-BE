package my.sleepydeveloper.projectcompass.domain.invitation.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class InviteCrewAccessDenied extends DefaultBusinessException {
    public InviteCrewAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
