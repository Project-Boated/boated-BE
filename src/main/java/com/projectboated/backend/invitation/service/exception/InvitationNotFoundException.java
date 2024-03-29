package com.projectboated.backend.invitation.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class InvitationNotFoundException extends BusinessException {
    public InvitationNotFoundException() {
        super(ErrorCode.INVITATION_NOT_FOUND);
    }

}
