package com.projectboated.backend.projectchatting.chattingroom.controller.dto.response;

import com.projectboated.backend.projectchatting.chattingroom.controller.dto.common.ChattingResponse;

import java.util.List;

public class GetProjectChattingRoomResponse {

    private Long id;

    private List<ChattingResponse> chattingResponses;

    public GetProjectChattingRoomResponse(Long id, List<ChattingResponse> chattingResponses) {
        this.id = id;
        this.chattingResponses = chattingResponses;
    }
}
