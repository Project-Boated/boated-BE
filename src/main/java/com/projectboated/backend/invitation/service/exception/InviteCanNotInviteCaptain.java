package com.projectboated.backend.invitation.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class InviteCanNotInviteCaptain extends BusinessException {
    public InviteCanNotInviteCaptain() {
        super(ErrorCode.INVITATION_DO_NOT_INVITE_CAPTAIN);
    }
}
