package com.projectboated.backend.domain.account.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountProfileImageFileNotExist extends BusinessException {
    public AccountProfileImageFileNotExist(ErrorCode errorCode) {
        super(errorCode);
    }

    public AccountProfileImageFileNotExist(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
