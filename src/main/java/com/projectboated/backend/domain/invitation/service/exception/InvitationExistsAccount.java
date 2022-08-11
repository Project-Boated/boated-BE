package com.projectboated.backend.domain.invitation.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class InvitationExistsAccount extends BusinessException {
    public InvitationExistsAccount() {
        super(ErrorCode.INVITATION_EXISTS);
    }
}
