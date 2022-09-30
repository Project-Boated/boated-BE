package com.projectboated.backend.invitation.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateInvitationResponse {
    private Long id;

    @Builder
    public CreateInvitationResponse(Long id) {
        this.id = id;
    }
}
