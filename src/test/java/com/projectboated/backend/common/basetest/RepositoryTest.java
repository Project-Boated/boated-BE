package com.projectboated.backend.common.basetest;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.domain.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataAccount.ROLES;
import static com.projectboated.backend.common.data.BasicDataProject.*;

@DataJpaTest
public class RepositoryTest extends BaseTest {

    @Autowired
    protected
    EntityManager em;

    @Autowired
    protected KanbanRepository kanbanRepository;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected AccountProjectRepository accountProjectRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected ProfileImageRepository profileImageRepository;


    protected void flushAndClear() {
        em.flush();
        em.clear();
    }

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

    protected Account insertDefaultAccount() {
        return accountRepository.save(Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .roles(ROLES)
                .build());
    }

    protected Account insertDefaultAccount2() {
        return accountRepository.save(Account.builder()
                .username(USERNAME2)
                .password(PASSWORD2)
                .nickname(NICKNAME2)
                .roles(ROLES)
                .build());
    }

    protected AccountProject insertAccountProject(Account account, Project project) {
        return accountProjectRepository.save(AccountProject.builder()
                .project(project)
                .account(account)
                .build());
    }


}
