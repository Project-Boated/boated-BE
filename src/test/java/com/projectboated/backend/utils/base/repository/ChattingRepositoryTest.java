package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.project.projectchatting.chatting.domain.Chatting;
import com.projectboated.backend.project.projectchatting.chatting.repository.ChattingRepository;
import com.projectboated.backend.project.projectchatting.chattingroom.domain.ChattingRoom;
import org.springframework.beans.factory.annotation.Autowired;

public class ChattingRepositoryTest extends ProjectChattingRoomRepositoryTest {

    @Autowired
    protected ChattingRepository chattingRepository;

    protected Chatting insertChatting(ChattingRoom chattingRoom, String body) {
        return chattingRepository.save(createChatting(chattingRoom, body));
    }

}
