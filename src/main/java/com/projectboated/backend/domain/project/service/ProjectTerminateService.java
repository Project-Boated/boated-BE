package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.project.service.exception.ProjectTerminateAccessDeniedException;
import com.projectboated.backend.domain.project.service.exception.ProjectUpdateAccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectTerminateService {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void terminateProject(Long accountId, Long projectId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.isCaptain(account)) {
            throw new ProjectTerminateAccessDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN);
        }

        project.terminate();
    }

    @Transactional
    public void cancelTerminateProject(Long accountId, Long projectId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.isCaptain(account)) {
            throw new ProjectUpdateAccessDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN);
        }

        project.cancelTerminate();
    }

}
