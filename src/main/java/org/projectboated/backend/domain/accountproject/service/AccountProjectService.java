package org.projectboated.backend.domain.accountproject.service;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.accountproject.repository.AccountProjectRepository;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.repository.ProjectRepository;
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
