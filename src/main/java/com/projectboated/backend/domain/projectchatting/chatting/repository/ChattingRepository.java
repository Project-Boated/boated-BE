package com.projectboated.backend.domain.projectchatting.chatting.repository;

import com.projectboated.backend.domain.projectchatting.chatting.domain.Chatting;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {

    @Query("select c from Chatting c where c.chattingRoom=:chattingRoom order by c.createdDate")
    List<Chatting> findByChattingRoom(@Param("chattingRoom") ChattingRoom chattingRoom);

}
