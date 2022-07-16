package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountProjectService {

    private final AccountProjectRepository accountProjectRepository;

    public List<Account> getCrews(Project project) {
        return accountProjectRepository.findCrewByProject(project);
    }

    public boolean isCrew(Project project,Account account) {
        return accountProjectRepository.countByCrewInProject(account, project) == 1;
    }

}
