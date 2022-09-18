package com.projectboated.backend.project.projectchatting.chattingroom.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class ProjectChattingRoomNotFoundException extends BusinessException {
    public ProjectChattingRoomNotFoundException() {
        super(ErrorCode.PROJECT_CHATTING_ROOM_NOT_FOUND);
    }
}
