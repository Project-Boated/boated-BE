package com.projectboated.backend.common.utils.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class MultiPartFileIsEmptyException extends BusinessException {
    public MultiPartFileIsEmptyException() {
        super(ErrorCode.COMMON_MULTIPART_FILE_IS_EMPTY);
    }
}
