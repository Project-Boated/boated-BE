package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.repository.ChattingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ChattingRoomRepositoryTest extends ProjectVideoRepositoryTest {

    @Autowired
    protected ChattingRoomRepository projectChattingRoomRepository;

    protected ChattingRoom insertChattingRoom() {
        return projectChattingRoomRepository.save(createChattingRoom());
    }

}
