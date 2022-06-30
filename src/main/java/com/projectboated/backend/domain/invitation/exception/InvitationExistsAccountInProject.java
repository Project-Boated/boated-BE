package com.projectboated.backend.domain.invitation.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class InvitationExistsAccountInProject extends BusinessException {
    public InvitationExistsAccountInProject(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvitationExistsAccountInProject(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
