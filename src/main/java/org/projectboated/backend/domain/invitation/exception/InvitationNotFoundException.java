package org.projectboated.backend.domain.invitation.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class InvitationNotFoundException extends BusinessException {
    public InvitationNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
