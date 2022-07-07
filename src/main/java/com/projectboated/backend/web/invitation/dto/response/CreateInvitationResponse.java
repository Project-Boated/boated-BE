package com.projectboated.backend.web.invitation.dto.response;

import lombok.*;

@Getter
public class CreateInvitationResponse {
    private Long id;

    @Builder
    public CreateInvitationResponse(Long id) {
        this.id = id;
    }
}
