package com.projectboated.backend.project.project.entity.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class ProjectTotalFileSizeIsMinusException extends BusinessException {
    public ProjectTotalFileSizeIsMinusException() {
        super(ErrorCode.PROJECT_TOTAL_FILE_SIZE_IS_MINUS);
    }
}
