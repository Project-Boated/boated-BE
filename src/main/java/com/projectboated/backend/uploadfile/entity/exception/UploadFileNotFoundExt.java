package com.projectboated.backend.uploadfile.entity.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class UploadFileNotFoundExt extends BusinessException {
    public UploadFileNotFoundExt(ErrorCode errorCode) {
        super(errorCode);
    }
}
