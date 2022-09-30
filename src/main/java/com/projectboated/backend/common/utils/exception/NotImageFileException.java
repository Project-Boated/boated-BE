package com.projectboated.backend.common.utils.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class NotImageFileException extends BusinessException {
    public NotImageFileException() {
        super(ErrorCode.COMMON_NOT_IMAGE_FILE_EXCEPTION);
    }
}
