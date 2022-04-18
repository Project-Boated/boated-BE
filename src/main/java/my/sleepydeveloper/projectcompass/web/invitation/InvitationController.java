package my.sleepydeveloper.projectcompass.web.invitation;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.invitation.InvitationService;
import my.sleepydeveloper.projectcompass.domain.invitation.entity.Invitation;
import my.sleepydeveloper.projectcompass.security.dto.IdDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/projects/{projectId}/crews?nickname={nickname}")
    public ResponseEntity<IdDto> inviteCrew(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable String nickname
    ) {
        Invitation invitation = invitationService.inviteCrew(projectId, nickname, account);
        return ResponseEntity.ok(new IdDto(invitation.getId()));
    }

}
