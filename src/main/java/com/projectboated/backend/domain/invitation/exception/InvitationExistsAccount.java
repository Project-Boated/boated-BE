package com.projectboated.backend.domain.invitation.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class InvitationExistsAccount extends BusinessException {
    public InvitationExistsAccount(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvitationExistsAccount(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
