package com.projectboated.backend.domain.invitation.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class InvitationNotFoundException extends BusinessException {
    public InvitationNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
