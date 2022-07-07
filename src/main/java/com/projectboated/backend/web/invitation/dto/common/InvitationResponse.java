package com.projectboated.backend.web.invitation.dto.common;

import com.projectboated.backend.domain.invitation.entity.Invitation;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InvitationResponse {

    private Long id;
    private LocalDateTime createdDate;
    private String name;
    private String description;
    private String captainNickname;

    public InvitationResponse(Invitation invitation) {
        this.id = invitation.getId();
        this.createdDate = invitation.getCreatedDate();
        this.name = invitation.getProject().getName();
        this.description = invitation.getProject().getDescription();
        this.captainNickname = invitation.getProject().getCaptain().getNickname();
    }
}