package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.projectchatting.chatting.domain.Chatting;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ProjectChattingRoom;

public class BaseChattingTest extends BaseProjectChattingRoomTest {

    protected Chatting createChatting(Long id, ChattingRoom chattingRoom, String body) {
        return Chatting.builder()
                .id(id)
                .chattingRoom(chattingRoom)
                .body(body)
                .build();
    }

    protected Chatting createChatting(ChattingRoom chattingRoom, String body) {
        return Chatting.builder()
                .chattingRoom(chattingRoom)
                .body(body)
                .build();
    }
}
