package com.projectboated.backend.domain.invitation.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class InviteCrewAccessDenied extends BusinessException {
    public InviteCrewAccessDenied(ErrorCode errorCode) {
        super(errorCode);
    }
}
