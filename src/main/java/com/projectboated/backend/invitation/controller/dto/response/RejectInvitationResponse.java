package com.projectboated.backend.invitation.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RejectInvitationResponse {
    private Long id;

    @Builder
    public RejectInvitationResponse(Long id) {
        this.id = id;
    }
}
