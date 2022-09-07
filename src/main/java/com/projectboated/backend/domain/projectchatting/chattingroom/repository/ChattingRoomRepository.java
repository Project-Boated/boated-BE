package com.projectboated.backend.domain.projectchatting.chattingroom.repository;

import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ProjectChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
}
