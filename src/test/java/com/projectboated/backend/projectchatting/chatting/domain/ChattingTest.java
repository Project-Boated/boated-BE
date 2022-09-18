package com.projectboated.backend.projectchatting.chatting.domain;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.projectchatting.chattingroom.domain.ChattingRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataChatting.CHATTING_BODY;
import static com.projectboated.backend.utils.data.BasicDataChatting.CHATTING_ID;

@DisplayName("Chatting : Entity 단위 테스트")
class ChattingTest {

    @Test
    void 생성자_정상적인param_생성성공() {
        // Given
        ChattingRoom chattingRoom = new ChattingRoom(CHATTING_ID);

        Account account = Account.builder()
                .build();

        // When
        // Then
        Chatting chatting = new Chatting(CHATTING_ID, chattingRoom, account, CHATTING_BODY);
    }

}