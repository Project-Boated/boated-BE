package com.projectboated.backend.projectchatting.chattingroom.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataChattingRoom.CHATTING_ROOM_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChattingRoom : Entity 단위 테스트")
class ChattingRoomTest {

    @Test
    void 생성자_모든param_정상생성() {
        // Given
        // When
        ChattingRoom chattingRoom = new ChattingRoom(CHATTING_ROOM_ID);

        // Then
        assertThat(chattingRoom.getId()).isEqualTo(CHATTING_ROOM_ID);
    }

}