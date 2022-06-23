package org.projectboated.backend.domain.invitation.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class InvitationExistsAccount extends BusinessException {
    public InvitationExistsAccount(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvitationExistsAccount(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
