package my.sleepydeveloper.projectcompass.domain.invitation;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNotFoundException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.invitation.entity.Invitation;
import my.sleepydeveloper.projectcompass.domain.invitation.exception.InviteCrewAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNotFoundException;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvitationService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;
    private final InvitationRepository invitationRepository;

    @Transactional
    public Invitation inviteCrew(Long projectId, String nickname, Account captain) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (project.getCaptain().getId() != captain.getId()) {
            throw new InviteCrewAccessDenied(ErrorCode.COMMON_ACCESS_DENIED);
        }

        Account account = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        return invitationRepository.save(new Invitation(account, project));
    }
}
