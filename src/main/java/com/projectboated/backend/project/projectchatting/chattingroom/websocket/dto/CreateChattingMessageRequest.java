package com.projectboated.backend.project.projectchatting.chattingroom.websocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateChattingMessageRequest {
    private String body;
}
