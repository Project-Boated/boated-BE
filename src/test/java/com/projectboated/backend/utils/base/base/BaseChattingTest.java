package com.projectboated.backend.utils.base.base;

import com.projectboated.backend.projectchatting.chatting.domain.Chatting;
import com.projectboated.backend.projectchatting.chattingroom.domain.ChattingRoom;

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
