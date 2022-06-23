package org.projectboated.backend.domain.invitation.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class InviteCrewAccessDenied extends BusinessException {
    public InviteCrewAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
