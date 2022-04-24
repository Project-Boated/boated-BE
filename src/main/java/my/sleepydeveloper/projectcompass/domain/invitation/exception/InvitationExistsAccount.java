package my.sleepydeveloper.projectcompass.domain.invitation.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class InvitationExistsAccount extends BusinessException {
    public InvitationExistsAccount(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvitationExistsAccount(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
