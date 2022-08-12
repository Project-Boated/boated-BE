package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;

import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;

public class BaseProjectTest extends BaseUploadFileTest {

    protected Project createProject(Account account) {
        return Project.builder()
                .id(PROJECT_ID)
                .captain(account)
                .build();
    }

    protected AccountProject createAccountProject(Account account, Project project) {
        return AccountProject.builder()
                .account(account)
                .project(project)
                .build();
    }

}
