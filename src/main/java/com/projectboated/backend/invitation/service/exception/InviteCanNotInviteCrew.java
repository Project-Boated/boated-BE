package com.projectboated.backend.invitation.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class InviteCanNotInviteCrew extends BusinessException {
    public InviteCanNotInviteCrew() {
        super(ErrorCode.INVITATION_DO_NOT_INVITE_CREW);
    }
}
