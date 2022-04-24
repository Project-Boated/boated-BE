package my.sleepydeveloper.projectcompass.domain.invitation;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.accountproject.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.accountproject.repository.AccountProjectRepository;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNotFoundException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.invitation.entity.Invitation;
import my.sleepydeveloper.projectcompass.domain.invitation.exception.InvitationExistsAccount;
import my.sleepydeveloper.projectcompass.domain.invitation.exception.InvitationExistsAccountInProject;
import my.sleepydeveloper.projectcompass.domain.invitation.exception.InvitationNotFoundException;
import my.sleepydeveloper.projectcompass.domain.invitation.exception.InviteCrewAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNotFoundException;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvitationService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;
    private final InvitationRepository invitationRepository;
    private final AccountProjectRepository accountProjectRepository;

    @Transactional
    public Invitation inviteCrew(Long projectId, String nickname, Account captain) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (project.getCaptain().getId() != captain.getId()) {
            throw new InviteCrewAccessDenied(ErrorCode.COMMON_ACCESS_DENIED);
        }

        Account crew = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (captain.getId() == crew.getId()) {
            throw new InviteCrewAccessDenied(ErrorCode.INVITATION_DO_NOT_INVITE_CAPTAIN);
        }

        invitationRepository.findByAccountAndProject(crew, project)
                .ifPresent(invitation -> {
                    throw new InvitationExistsAccount(ErrorCode.INVITATION_ACCOUNT_EXISTS);
                });

        if (!accountProjectRepository.existsAccountInProject(crew, project)) {
            throw new InvitationExistsAccountInProject(ErrorCode.INVITATION_ACCOUNT_EXISTS_IN_PROJECT);
        }

        return invitationRepository.save(new Invitation(crew, project));
    }

    public List<Invitation> findByAccount(Account account) {
        return invitationRepository.findByAccount(account);
    }

    @Transactional
    public void accept(Account account, Long invitationId) {
        Account crew = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(ErrorCode.INVITATION_NOT_FOUND));

        Project project = invitation.getProject();

        accountProjectRepository.save(new AccountProject(crew, project));

        invitationRepository.delete(invitation);
    }

    @Transactional
    public void reject(Account account, Long invitationId) {
        Account crew = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(ErrorCode.INVITATION_NOT_FOUND));

        invitationRepository.delete(invitation);
    }
}
