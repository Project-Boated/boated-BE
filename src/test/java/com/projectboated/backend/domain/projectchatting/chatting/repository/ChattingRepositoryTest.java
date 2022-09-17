package com.projectboated.backend.domain.projectchatting.chatting.repository;

import com.projectboated.backend.utils.base.RepositoryTest;
import com.projectboated.backend.domain.projectchatting.chatting.domain.Chatting;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.projectboated.backend.utils.data.BasicDataChatting.CHATTING_BODY;
import static com.projectboated.backend.utils.data.BasicDataChatting.CHATTING_BODY2;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Chatting : Persistence 단위 테스트")
class ChattingRepositoryTest extends RepositoryTest {
    
    @Test
    void findByChattingRoom_채팅1개존재_return_1개(){
        // Given
        ChattingRoom chattingRoom = insertChattingRoom();
        Chatting chatting = insertChatting(chattingRoom, CHATTING_BODY);

        // When
        List<Chatting> result = chattingRepository.findByChattingRoom(chattingRoom);

        // Then
        assertThat(result).containsExactly(chatting);
    }

    @Test
    void findByChattingRoom_다른room에채팅존재_return_empty(){
        // Given
        ChattingRoom chattingRoom = insertChattingRoom();
        ChattingRoom chattingRoom2 = insertChattingRoom();
        Chatting chatting2 = insertChatting(chattingRoom2, CHATTING_BODY2);

        // When
        List<Chatting> result = chattingRepository.findByChattingRoom(chattingRoom);

        // Then
        assertThat(result).isEmpty();
    }

}