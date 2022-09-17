package com.projectboated.backend.account.profileimage.entity.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProfileImageNeedsHostUrlException extends BusinessException {
    public ProfileImageNeedsHostUrlException(ErrorCode errorCode) {
        super(errorCode);
    }
}
