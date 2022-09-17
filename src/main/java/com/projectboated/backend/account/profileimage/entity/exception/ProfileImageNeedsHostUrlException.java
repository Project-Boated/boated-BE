package com.projectboated.backend.account.profileimage.entity.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class ProfileImageNeedsHostUrlException extends BusinessException {
    public ProfileImageNeedsHostUrlException(ErrorCode errorCode) {
        super(errorCode);
    }
}
