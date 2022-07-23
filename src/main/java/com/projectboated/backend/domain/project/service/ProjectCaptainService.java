package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.project.service.exception.ProjectCaptainUpdateAccessDeniedException;
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
    public void updateCaptain(Long accountId, Long projectId, String newCaptainNickname) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.isCaptain(account)) {
            throw new ProjectCaptainUpdateAccessDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN);
        }

        Account newCaptain = accountRepository.findByNickname(newCaptainNickname)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND_BY_USERNAME));

        if(!projectService.isCrew(project, newCaptain)) {
            throw new ProjectCaptainUpdateAccessDeniedException(ErrorCode.PROJECT_CAPTAIN_UPDATE_DENIED_NOT_CREW);
        }

        accountProjectRepository.save(new AccountProject(account, project));
        accountProjectRepository.deleteByProjectAndAccount(project, newCaptain);
        project.changeCaptain(newCaptain);
    }

}
