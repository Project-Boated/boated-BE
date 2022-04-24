package my.sleepydeveloper.projectcompass.domain.invitation.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class InvitationExistsAccountInProject extends BusinessException {
    public InvitationExistsAccountInProject(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvitationExistsAccountInProject(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
