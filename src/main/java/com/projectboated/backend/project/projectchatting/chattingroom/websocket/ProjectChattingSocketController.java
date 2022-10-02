package com.projectboated.backend.project.projectchatting.chattingroom.websocket;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.projectchatting.chatting.domain.Chatting;
import com.projectboated.backend.project.projectchatting.chattingroom.service.ProjectChattingRoomService;
import com.projectboated.backend.project.projectchatting.chattingroom.websocket.dto.CreateChattingMessageRequest;
import com.projectboated.backend.project.projectchatting.chattingroom.websocket.dto.CreateChattingMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProjectChattingSocketController {

    private final ProjectChattingRoomService projectChattingRoomService;

    @MessageMapping("/project/{projectId}/chatting/post")
    public CreateChattingMessageResponse sendChattingMessage(
            @AuthenticationPrincipal Account account,
            @DestinationVariable Long projectId,
            @Payload CreateChattingMessageRequest createChattingMessageRequest
    ) {
        Chatting chatting = projectChattingRoomService.saveChatting(account.getId(), projectId, createChattingMessageRequest);
        return new CreateChattingMessageResponse(chatting);
    }

}
