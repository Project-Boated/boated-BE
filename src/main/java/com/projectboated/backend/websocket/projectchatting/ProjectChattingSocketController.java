package com.projectboated.backend.websocket.projectchatting;

import com.projectboated.backend.domain.projectchatting.chattingroom.service.ProjectChattingRoomService;
import com.projectboated.backend.websocket.projectchatting.dto.ChattingMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProjectChattingSocketController {

    private final ProjectChattingRoomService projectChattingRoomService;

    @MessageMapping("/project/{projectId}/chatting")
    public ChattingMessage sendChattingMessage(
            @DestinationVariable Long projectId,
            @Payload ChattingMessage chattingMessage
    ) {
//        projectChattingRoomService.saveChatting(projectId, chattingMessage);
//        return chattingMessage;
        return null;
    }

}
