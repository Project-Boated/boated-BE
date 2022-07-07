package com.projectboated.backend.web.invitation.dto.response;


import lombok.*;

@Getter
public class AcceptInvitationResponse {

    private Long id;

    @Builder
    public AcceptInvitationResponse(Long id) {
        this.id = id;
    }
}
