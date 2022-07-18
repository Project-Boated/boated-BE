package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectCaptainUpdateAccessDeniedException;
import com.projectboated.backend.domain.project.service.exception.ProjectFindAllCrewsAccessDeniedException;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectCrewService {

    private final AccountProjectService accountProjectService;
    private final ProjectRepository projectRepository;
    private final AccountProjectRepository accountProjectRepository;
    private final AccountRepository accountRepository;

    public List<Account> findAllCrews(Long accountId, Long projectId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.isCaptain(account) &&
                !accountProjectService.isCrew(project, account)) {
            throw new ProjectFindAllCrewsAccessDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
        }

        return accountProjectRepository.findCrewByProject(project);
    }

    @Transactional
    public void deleteCrew(Account account, Long projectId, String crewNickname) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!(project.getCaptain().getId() == account.getId())) {
            throw new ProjectCaptainUpdateAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        Account crew = accountRepository.findByNickname(crewNickname)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND_BY_USERNAME));

        accountProjectRepository.deleteByProjectAndAccount(project, crew);
    }

}
