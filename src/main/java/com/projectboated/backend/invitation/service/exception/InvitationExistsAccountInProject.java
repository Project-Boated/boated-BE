package com.projectboated.backend.invitation.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class InvitationExistsAccountInProject extends BusinessException {
    public InvitationExistsAccountInProject(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvitationExistsAccountInProject(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
