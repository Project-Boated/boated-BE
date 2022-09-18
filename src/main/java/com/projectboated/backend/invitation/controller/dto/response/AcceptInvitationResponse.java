package com.projectboated.backend.invitation.controller.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
public class AcceptInvitationResponse {

    private Long id;

    @Builder
    public AcceptInvitationResponse(Long id) {
        this.id = id;
    }
}
