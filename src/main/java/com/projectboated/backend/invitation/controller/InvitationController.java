package com.projectboated.backend.invitation.controller;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.invitation.controller.dto.response.AcceptInvitationResponse;
import com.projectboated.backend.invitation.controller.dto.response.CreateInvitationResponse;
import com.projectboated.backend.invitation.controller.dto.response.GetMyInvitationResponse;
import com.projectboated.backend.invitation.controller.dto.response.RejectInvitationResponse;
import com.projectboated.backend.invitation.entity.Invitation;
import com.projectboated.backend.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/projects/{projectId}/crews")
    public CreateInvitationResponse createInvitation(
            @PathVariable Long projectId,
            @RequestParam String nickname
    ) {
        Invitation invitation = invitationService.inviteCrew(projectId, nickname);
        return new CreateInvitationResponse(invitation.getId());
    }

    @GetMapping("/account/invitations")
    public GetMyInvitationResponse getMyInvitation(@AuthenticationPrincipal Account account) {
        return new GetMyInvitationResponse(invitationService.findByAccount(account));
    }

    @PostMapping("/account/invitations/{invitationId}/accept")
    public AcceptInvitationResponse acceptInvitation(@AuthenticationPrincipal Account account,
                                                     @PathVariable Long invitationId) {
        Long projectId = invitationService.accept(account.getId(), invitationId);
        return new AcceptInvitationResponse(projectId);
    }

    @PostMapping("/account/invitations/{invitationId}/reject")
    public RejectInvitationResponse rejectInvitation(@AuthenticationPrincipal Account account,
                                                     @PathVariable Long invitationId) {
        Long projectId = invitationService.reject(account.getId(), invitationId);
        return new RejectInvitationResponse(projectId);
    }

}
