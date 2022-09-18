package com.projectboated.backend.project.project.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.project.project.aop.OnlyCaptain;
import com.projectboated.backend.project.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.repository.AccountProjectRepository;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.project.service.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectCrewService {

    private final ProjectRepository projectRepository;
    private final AccountProjectRepository accountProjectRepository;
    private final AccountRepository accountRepository;

    @OnlyCaptainOrCrew
    public List<Account> findAllCrews(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        return accountProjectRepository.findCrewByProject(project);
    }

    @Transactional
    @OnlyCaptain
    public void deleteCrew(Long projectId, String crewNickname) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        Account crew = accountRepository.findByNickname(crewNickname)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND_BY_USERNAME));

        accountProjectRepository.deleteByProjectAndAccount(project, crew);
    }

}
