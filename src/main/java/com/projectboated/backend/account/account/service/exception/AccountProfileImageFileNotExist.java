package com.projectboated.backend.account.account.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountProfileImageFileNotExist extends BusinessException {
    public AccountProfileImageFileNotExist(ErrorCode errorCode) {
        super(errorCode);
    }
}
