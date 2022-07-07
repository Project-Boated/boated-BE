package com.projectboated.backend.web.invitation.dto.response;

import lombok.*;

@Getter
public class RejectInvitationResponse {
    private Long id;

    @Builder
    public RejectInvitationResponse(Long id) {
        this.id = id;
    }
}
