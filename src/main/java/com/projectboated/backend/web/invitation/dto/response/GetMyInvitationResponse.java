package com.projectboated.backend.web.invitation.dto.response;

import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.web.invitation.dto.common.InvitationResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
