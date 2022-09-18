package com.projectboated.backend.project.project.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.project.project.aop.OnlyCaptain;
import com.projectboated.backend.project.project.entity.AccountProject;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.repository.AccountProjectRepository;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.project.service.exception.ProjectCaptainUpdateAccessDeniedException;
import com.projectboated.backend.project.project.service.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectCaptainService {
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;
    private final AccountProjectRepository accountProjectRepository;

    @Transactional
    @OnlyCaptain
    public void updateCaptain(Long accountId, Long projectId, String newCaptainNickname) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        Account newCaptain = accountRepository.findByNickname(newCaptainNickname)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND_BY_USERNAME));

        if (!projectService.isCrew(project, newCaptain)) {
            throw new ProjectCaptainUpdateAccessDeniedException(ErrorCode.PROJECT_CAPTAIN_UPDATE_DENIED_NOT_CREW);
        }

        accountProjectRepository.save(AccountProject.builder()
                .account(account)
                .project(project)
                .build());
        accountProjectRepository.deleteByProjectAndAccount(project, newCaptain);
        project.changeCaptain(newCaptain);
    }

}
