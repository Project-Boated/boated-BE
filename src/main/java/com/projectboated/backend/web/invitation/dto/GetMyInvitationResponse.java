package com.projectboated.backend.web.invitation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.invitation.entity.Invitation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetMyInvitationResponse {

    List<InvitationResponse> invitations = new ArrayList<>();

    public GetMyInvitationResponse(List<Invitation> invitations) {
        this.invitations = invitations.stream()
                .map(InvitationResponse::new)
                .collect(Collectors.toList());
    }

    @Getter
    @NoArgsConstructor
    static class InvitationResponse {

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
}
