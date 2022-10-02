package com.projectboated.backend.project.projectchatting.chattingroom.websocket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.project.projectchatting.chatting.domain.Chatting;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateChattingMessageResponse {

    private Long id;

    private String username;

    private String body;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    public CreateChattingMessageResponse(Chatting chatting) {
        this.id = chatting.getId();
        this.username = chatting.getAccount().getUsername();
        this.body = chatting.getBody();
        this.createdDate = chatting.getCreatedDate();
    }
}
