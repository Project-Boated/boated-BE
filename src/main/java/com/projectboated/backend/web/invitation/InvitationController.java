package com.projectboated.backend.web.invitation;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.web.invitation.dto.CreateInvitationResponse;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.invitation.service.InvitationService;
import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.web.invitation.dto.AcceptInvitationResponse;
import com.projectboated.backend.web.invitation.dto.GetMyInvitationResponse;
import com.projectboated.backend.web.invitation.dto.RejectInvitationResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/projects/{projectId}/crews")
    public CreateInvitationResponse createInvitation(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestParam String nickname
    ) {
        Invitation invitation = invitationService.inviteCrew(projectId, nickname, account);
        return new CreateInvitationResponse(invitation.getId());
    }

    @GetMapping("/account/invitations")
    public GetMyInvitationResponse getMyInvitation(@AuthenticationPrincipal Account account) {
        return new GetMyInvitationResponse(invitationService.findByAccount(account));
    }

    @PostMapping("/account/invitations/{invitationId}/accept")
    public AcceptInvitationResponse acceptInvitation(@AuthenticationPrincipal Account account,
                                                     @PathVariable Long invitationId) {
        Long projectId = invitationService.accept(account, invitationId);
        return new AcceptInvitationResponse(projectId);
    }

    @PostMapping("/account/invitations/{invitationId}/reject")
    public RejectInvitationResponse rejectInvitation(@AuthenticationPrincipal Account account,
                                                     @PathVariable Long invitationId) {
        Long projectId = invitationService.reject(account, invitationId);
        return new RejectInvitationResponse(projectId);
    }

}
