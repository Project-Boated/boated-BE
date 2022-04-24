package my.sleepydeveloper.projectcompass.domain.invitation.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class InvitationNotFoundException extends BusinessException {
    public InvitationNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
