package com.projectboated.backend.project.projectchatting.chattingroom.repository;

import com.projectboated.backend.project.projectchatting.chattingroom.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
}
