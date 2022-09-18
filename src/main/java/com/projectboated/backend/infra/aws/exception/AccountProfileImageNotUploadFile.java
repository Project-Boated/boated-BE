package com.projectboated.backend.infra.aws.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class AccountProfileImageNotUploadFile extends BusinessException {
    public AccountProfileImageNotUploadFile(ErrorCode errorCode) {
        super(errorCode);
    }
}
