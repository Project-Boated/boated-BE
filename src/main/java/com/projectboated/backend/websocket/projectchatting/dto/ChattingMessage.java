package com.projectboated.backend.websocket.projectchatting.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Getter
public class ChattingMessage {

    private ChattingMessageType messageType;
    private String body;

    @Builder
    public ChattingMessage(ChattingMessageType messageType, String body) {
        this.messageType = messageType;
        this.body = body;
    }
}