package com.projectboated.backend.web.projectchatting.dto.response;

import com.projectboated.backend.web.projectchatting.dto.common.ChattingResponse;

import java.util.List;

public class GetProjectChattingRoomResponse {

    private Long id;

    private List<ChattingResponse> chattingResponses;

    public GetProjectChattingRoomResponse(Long id, List<ChattingResponse> chattingResponses) {
        this.id = id;
        this.chattingResponses = chattingResponses;
    }
}
