package com.projectboated.backend.utils.basetest.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class ProjectRepositoryTest extends ProfileImageRepositoryTest {

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected AccountProjectRepository accountProjectRepository;

    protected Project insertProject(Account captain) {
        return projectRepository.save(createProject(captain));
    }

    protected Project insertProject(Account captain, LocalDateTime createdDate, LocalDateTime deadline) {
        return projectRepository.save(createProject(captain, createdDate, deadline));
    }

    protected Project insertProjectAndCaptain(String username, String nickname) {
        Account account = accountRepository.save(createAccount(username, nickname));
        return projectRepository.save(createProject(account));
    }

    protected Project insertProjectAndCaptain() {
        Account account = accountRepository.save(createAccount());
        return projectRepository.save(createProject(account));
    }

    protected AccountProject insertAccountProject(Account account, Project project) {
        return accountProjectRepository.save(createAccountProject(account, project));
    }
}
