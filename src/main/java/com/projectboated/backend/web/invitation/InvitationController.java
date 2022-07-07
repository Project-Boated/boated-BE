package com.projectboated.backend.web.invitation;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.web.invitation.dto.response.CreateInvitationResponse;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.invitation.service.InvitationService;
import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.web.invitation.dto.response.AcceptInvitationResponse;
import com.projectboated.backend.web.invitation.dto.response.GetMyInvitationResponse;
import com.projectboated.backend.web.invitation.dto.response.RejectInvitationResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/api/projects/{projectId}/crews")
    public CreateInvitationResponse createInvitation(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestParam String nickname
    ) {
        Invitation invitation = invitationService.inviteCrew(projectId, nickname, account);
        return new CreateInvitationResponse(invitation.getId());
    }

    @GetMapping("/api/account/invitations")
    public GetMyInvitationResponse getMyInvitation(@AuthenticationPrincipal Account account) {
        return new GetMyInvitationResponse(invitationService.findByAccount(account));
    }

    @PostMapping("/api/account/invitations/{invitationId}/accept")
    public AcceptInvitationResponse acceptInvitation(@AuthenticationPrincipal Account account,
                                                     @PathVariable Long invitationId) {
        Long projectId = invitationService.accept(account, invitationId);
        return new AcceptInvitationResponse(projectId);
    }

    @PostMapping("/api/account/invitations/{invitationId}/reject")
    public RejectInvitationResponse rejectInvitation(@AuthenticationPrincipal Account account,
                                                     @PathVariable Long invitationId) {
        Long projectId = invitationService.reject(account, invitationId);
        return new RejectInvitationResponse(projectId);
    }

}
