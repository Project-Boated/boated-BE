package com.projectboated.backend.domain.projectchatting.chatting.domain;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ProjectChattingRoom;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataChatting.CHATTING_BODY;
import static com.projectboated.backend.common.data.BasicDataChatting.CHATTING_ID;
import static com.projectboated.backend.common.data.BasicDataChattingRoom.CHATTING_ROOM_ID;

@DisplayName("Chatting : Entity 단위 테스트")
class ChattingTest {

    @Test
    void 생성자_정상적인param_생성성공(){
        // Given
        ChattingRoom chattingRoom = new ChattingRoom(CHATTING_ID);

        Account account = Account.builder()
                .build();

        // When
        // Then
        Chatting chatting = new Chatting(CHATTING_ID, chattingRoom, account, CHATTING_BODY);
    }

}