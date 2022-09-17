package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.domain.projectchatting.chatting.domain.Chatting;
import com.projectboated.backend.domain.projectchatting.chatting.repository.ChattingRepository;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import org.springframework.beans.factory.annotation.Autowired;

public class ChattingRepositoryTest extends ProjectChattingRoomRepositoryTest {

    @Autowired
    protected ChattingRepository chattingRepository;

    protected Chatting insertChatting(ChattingRoom chattingRoom, String body) {
        return chattingRepository.save(createChatting(chattingRoom, body));
    }

}
