package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ProjectChattingRoom;
import com.projectboated.backend.domain.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;

public class BaseChattingRoomTest extends BaseProjectVideoTest {

    protected ChattingRoom createChattingRoom() {
        return ProjectChattingRoom.builder()
                .build();
    }

    protected ChattingRoom createChattingRoom(Long id) {
        return ProjectChattingRoom.builder()
                .id(id)
                .build();
    }

}
