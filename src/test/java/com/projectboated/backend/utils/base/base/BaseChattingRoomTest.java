package com.projectboated.backend.utils.base.base;

import com.projectboated.backend.projectchatting.chattingroom.domain.ChattingRoom;
import com.projectboated.backend.projectchatting.chattingroom.domain.ProjectChattingRoom;

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
