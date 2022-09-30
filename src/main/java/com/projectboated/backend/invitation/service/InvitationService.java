package com.projectboated.backend.invitation.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.project.project.aop.OnlyCaptain;
import com.projectboated.backend.project.project.entity.AccountProject;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.repository.AccountProjectRepository;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.invitation.entity.Invitation;
import com.projectboated.backend.invitation.repository.InvitationRepository;
import com.projectboated.backend.invitation.service.exception.InvitationExistsAccount;
import com.projectboated.backend.invitation.service.exception.InvitationNotFoundException;
import com.projectboated.backend.invitation.service.exception.InviteCanNotInviteCaptain;
import com.projectboated.backend.invitation.service.exception.InviteCanNotInviteCrew;
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
