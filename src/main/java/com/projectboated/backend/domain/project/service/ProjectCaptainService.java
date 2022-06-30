package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.domain.accountproject.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.account.exception.AccountNotFoundException;
import com.projectboated.backend.domain.account.repository.AccountRepository;
import com.projectboated.backend.domain.accountproject.entity.AccountProject;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.project.exception.ProjectCaptainUpdateDenied;
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
    public void updateCaptain(Account account, Long projectId, String newCaptainNickname) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!projectService.isCaptain(account, project)) {
            throw new ProjectCaptainUpdateDenied(ErrorCode.PROJECT_CAPTAIN_UPDATE_DENIED_NOT_CAPTAIN);
        }

        Account newCaptain = accountRepository.findByNickname(newCaptainNickname)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND_BY_USERNAME));

        if(!projectService.isCrew(newCaptain, projectId)) {
            throw new ProjectCaptainUpdateDenied(ErrorCode.PROJECT_CAPTAIN_UPDATE_DENIED_NOT_CREW);
        }

        accountProjectRepository.save(new AccountProject(account, project));
        accountProjectRepository.delete(project, newCaptain);
        project.changeCaptain(newCaptain);
    }

}