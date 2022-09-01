package com.projectboated.backend.domain.projectchatting.projectchattingroom.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectChattingRoomNotFoundException extends BusinessException {
    public ProjectChattingRoomNotFoundException() {
        super(ErrorCode.PROJECT_CHATTING_ROOM_NOT_FOUND);
    }
}
