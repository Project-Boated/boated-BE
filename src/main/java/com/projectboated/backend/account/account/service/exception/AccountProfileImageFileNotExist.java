package com.projectboated.backend.account.account.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class AccountProfileImageFileNotExist extends BusinessException {
    public AccountProfileImageFileNotExist() {
        super(ErrorCode.ACCOUNT_PROFILE_IMAGE_FILE_NOT_EXIST);
    }
}
