package com.projectboated.backend.infra.aws.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class AccountProfileImageNotUploadFile extends BusinessException {
    public AccountProfileImageNotUploadFile() {
        super(ErrorCode.ACCOUNT_PROFILE_IMAGE_NOT_UPLOAD_FILE);
    }
}
