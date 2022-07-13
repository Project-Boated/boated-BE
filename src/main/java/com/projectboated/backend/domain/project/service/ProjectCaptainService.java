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
import com.projectboated.backend.domain.project.service.exception.ProjectCaptainUpdateDenied;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectCaptainService {

    private final ProjectService projectService;
    private final AccountProjectService accountProjectService;
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

        if(!accountProjectService.isCrew(project, newCaptain)) {
            throw new ProjectCaptainUpdateDenied(ErrorCode.PROJECT_CAPTAIN_UPDATE_DENIED_NOT_CREW);
        }

        accountProjectRepository.save(new AccountProject(account, project));
        accountProjectRepository.deleteByProjectAndAccount(project, newCaptain);
        project.changeCaptain(newCaptain);
    }

}
