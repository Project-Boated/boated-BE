package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectchatting.chatting.domain.Chatting;
import com.projectboated.backend.domain.projectchatting.chatting.repository.ChattingRepository;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ProjectChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.repository.ChattingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ChattingRepositoryTest extends ProjectChattingRoomRepositoryTest {

    @Autowired
    protected ChattingRepository chattingRepository;

    protected Chatting insertChatting(ChattingRoom chattingRoom, String body) {
        return chattingRepository.save(createChatting(chattingRoom, body));
    }

}
