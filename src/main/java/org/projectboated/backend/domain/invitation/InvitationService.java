package org.projectboated.backend.domain.invitation;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.accountproject.entity.AccountProject;
import org.projectboated.backend.domain.accountproject.repository.AccountProjectRepository;
import org.projectboated.backend.domain.exception.ErrorCode;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.exception.AccountNotFoundException;
import org.projectboated.backend.domain.account.repository.AccountRepository;
import org.projectboated.backend.domain.invitation.entity.Invitation;
import org.projectboated.backend.domain.invitation.exception.InvitationExistsAccount;
import org.projectboated.backend.domain.invitation.exception.InvitationExistsAccountInProject;
import org.projectboated.backend.domain.invitation.exception.InvitationNotFoundException;
import org.projectboated.backend.domain.invitation.exception.InviteCrewAccessDenied;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.exception.ProjectNotFoundException;
import org.projectboated.backend.domain.project.repository.ProjectRepository;
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
    public Long accept(Account account, Long invitationId) {
        Account crew = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(ErrorCode.INVITATION_NOT_FOUND));

        Project project = invitation.getProject();

        accountProjectRepository.save(new AccountProject(crew, project));

        invitationRepository.delete(invitation);

        return project.getId();
    }

    @Transactional
    public Long reject(Account account, Long invitationId) {
        Account crew = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(ErrorCode.INVITATION_NOT_FOUND));

        Long projectId = invitation.getProject().getId();

        invitationRepository.delete(invitation);

        return projectId;
    }
}
