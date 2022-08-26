package com.projectboated.backend.domain.projectvideo.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectVideoNotFoundException extends BusinessException {
    public ProjectVideoNotFoundException() {
        super(ErrorCode.PROJECT_VIDEO_NOT_FOUND);
    }
}
