package com.projectboated.backend.account.profileimage.entity.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class ProfileImageNeedsHostUrlException extends BusinessException {
    public ProfileImageNeedsHostUrlException() {
        super(ErrorCode.PROFILE_IMAGE_NEEDS_HOST_URL);
    }
}
