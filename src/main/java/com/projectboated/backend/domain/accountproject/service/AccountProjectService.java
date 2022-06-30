package com.projectboated.backend.domain.accountproject.service;

import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.domain.accountproject.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountProjectService {

    private final ProjectRepository projectRepository;
    private final AccountProjectRepository accountProjectRepository;

    public List<Account> getCrews(Project project) {
        return accountProjectRepository.findCrewsFromProject(project);
    }

}
