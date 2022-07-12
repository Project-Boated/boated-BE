package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static com.projectboated.backend.common.data.BasicDataProject.*;

public class ProjectRepositoryTest extends ProfileImageRepositoryTest{

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected AccountProjectRepository accountProjectRepository;


    protected Project insertDefaultProjectAndDefaultCaptain() {
        return projectRepository.save(Project.builder()
                .captain(insertDefaultAccount())
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build());
    }

    protected Project insertDefaultProject(Account captain) {
        return projectRepository.save(Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build());
    }

    protected Project insertDefaultProject2(Account captain) {
        return projectRepository.save(Project.builder()
                .captain(captain)
                .name(PROJECT_NAME2)
                .description(PROJECT_DESCRIPTION2)
                .deadline(PROJECT_DEADLINE2)
                .build());
    }



    protected AccountProject insertAccountProject(Account account, Project project) {
        return accountProjectRepository.save(AccountProject.builder()
                .project(project)
                .account(account)
                .build());
    }
}
