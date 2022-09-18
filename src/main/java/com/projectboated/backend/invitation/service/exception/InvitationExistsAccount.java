package com.projectboated.backend.invitation.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class InvitationExistsAccount extends BusinessException {
    public InvitationExistsAccount() {
        super(ErrorCode.INVITATION_EXISTS);
    }
}
