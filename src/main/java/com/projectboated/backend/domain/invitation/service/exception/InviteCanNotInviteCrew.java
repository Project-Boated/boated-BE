package com.projectboated.backend.domain.invitation.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class InviteCanNotInviteCrew extends BusinessException {
    public InviteCanNotInviteCrew() {
        super(ErrorCode.INVITATION_DO_NOT_INVITE_CREW);
    }
}
