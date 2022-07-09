package com.projectboated.backend.domain.uploadfile.entity.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class UploadFileNotFoundExt extends BusinessException {
    public UploadFileNotFoundExt(ErrorCode errorCode) {
        super(errorCode);
    }
}
