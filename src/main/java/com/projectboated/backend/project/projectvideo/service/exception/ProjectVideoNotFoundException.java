package com.projectboated.backend.project.projectvideo.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class ProjectVideoNotFoundException extends BusinessException {
    public ProjectVideoNotFoundException() {
        super(ErrorCode.PROJECT_VIDEO_NOT_FOUND);
    }
}
