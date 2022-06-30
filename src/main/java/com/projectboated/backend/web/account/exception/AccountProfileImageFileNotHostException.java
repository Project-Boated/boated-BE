package com.projectboated.backend.web.account.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountProfileImageFileNotHostException extends BusinessException {
    public AccountProfileImageFileNotHostException(ErrorCode errorCode) {
        super(errorCode);
    }
}
