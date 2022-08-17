package com.projectboated.backend.domain.invitation.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.domain.invitation.repository.InvitationRepository;
import com.projectboated.backend.domain.invitation.service.exception.InvitationExistsAccount;
import com.projectboated.backend.domain.invitation.service.exception.InvitationNotFoundException;
import com.projectboated.backend.domain.invitation.service.exception.InviteCanNotInviteCaptain;
import com.projectboated.backend.domain.invitation.service.exception.InviteCanNotInviteCrew;
import com.projectboated.backend.domain.project.aop.OnlyCaptain;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
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
    @OnlyCaptain
    public Invitation inviteCrew(Long projectId, String nickname) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        Account crew = accountRepository.findByNickname(nickname)
                .orElseThrow(AccountNotFoundException::new);

        if (project.getCaptain().equals(crew)) {
            throw new InviteCanNotInviteCaptain();
        }

        accountProjectRepository.findByAccountAndProject(crew, project)
                .ifPresent(ap -> {
                    throw new InviteCanNotInviteCrew();
                });

        invitationRepository.findByAccountAndProject(crew, project)
                .ifPresent(invitation -> {
                    throw new InvitationExistsAccount();
                });

        return invitationRepository.save(new Invitation(123L, crew, project));
    }

    public List<Invitation> findByAccount(Account account) {
        return invitationRepository.findByAccount(account);
    }

    @Transactional
    public Long accept(Long accountId, Long invitationId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Invitation invitation = invitationRepository.findByIdAndAccount(invitationId, account)
                .orElseThrow(InvitationNotFoundException::new);

        Project project = invitation.getProject();
        accountProjectRepository.save(AccountProject.builder()
                .account(account)
                .project(project)
                .build());

        invitationRepository.delete(invitation);

        return project.getId();
    }

    @Transactional
    public Long reject(Long accountId, Long invitationId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Invitation invitation = invitationRepository.findByIdAndAccount(invitationId, account)
                .orElseThrow(InvitationNotFoundException::new);

        invitationRepository.delete(invitation);

        return invitation.getProject().getId();
    }
}
