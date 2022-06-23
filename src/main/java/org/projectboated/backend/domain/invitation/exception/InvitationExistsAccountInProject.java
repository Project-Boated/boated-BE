package org.projectboated.backend.domain.invitation.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class InvitationExistsAccountInProject extends BusinessException {
    public InvitationExistsAccountInProject(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvitationExistsAccountInProject(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
