package com.projectboated.backend.utils.base.base;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.entity.AccountProject;
import com.projectboated.backend.project.project.entity.Project;

import java.time.LocalDateTime;

import static com.projectboated.backend.utils.data.BasicDataProject.*;

public class BaseProjectTest extends BaseUploadFileTest {

    protected Project createProject(Account account) {
        Project project = Project.builder()
                .captain(account)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        project.changeCreatedDate(LocalDateTime.now());
        return project;
    }

    protected Project createProject(Long id, Account account) {
        Project project = Project.builder()
                .id(id)
                .captain(account)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        project.changeCreatedDate(LocalDateTime.now());
        return project;
    }

    protected Project createProject(Account account, LocalDateTime createdDate, LocalDateTime deadline) {
        Project project = Project.builder()
                .captain(account)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(deadline)
                .build();
        project.changeCreatedDate(createdDate);
        return project;
    }

    protected Project createProject(Long id, Account account, LocalDateTime createdDate, LocalDateTime deadline) {
        Project project = Project.builder()
                .id(id)
                .captain(account)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(deadline)
                .build();
        project.changeCreatedDate(createdDate);
        return project;
    }

    protected Project createProjectAndCaptain(Long projectId, Long accountId) {
        Project project = Project.builder()
                .id(projectId)
                .captain(createAccount(accountId))
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        project.changeCreatedDate(LocalDateTime.now());
        return project;
    }

    protected Project createProjectAndCaptain() {
        Project project = Project.builder()
                .captain(createAccount())
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        project.changeCreatedDate(LocalDateTime.now());
        return project;
    }

    protected AccountProject createAccountProject(Long id, Account account, Project project) {
        return AccountProject.builder()
                .id(id)
                .account(account)
                .project(project)
                .build();
    }

    protected AccountProject createAccountProject(Account account, Project project) {
        return AccountProject.builder()
                .account(account)
                .project(project)
                .build();
    }

}
