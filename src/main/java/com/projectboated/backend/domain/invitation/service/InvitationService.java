package com.projectboated.backend.domain.invitation.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.invitation.repository.InvitationRepository;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.domain.invitation.service.exception.InvitationExistsAccount;
import com.projectboated.backend.domain.invitation.service.exception.InvitationExistsAccountInProject;
import com.projectboated.backend.domain.invitation.service.exception.InvitationNotFoundException;
import com.projectboated.backend.domain.invitation.service.exception.InviteCrewAccessDenied;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
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
