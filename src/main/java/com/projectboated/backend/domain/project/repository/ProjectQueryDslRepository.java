package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.domain.project.condition.GetMyProjectsCond;
import com.projectboated.backend.domain.project.entity.Project;

import java.util.List;

public interface ProjectQueryDslRepository {

    List<Project> getMyProjects(Account account, GetMyProjectsCond cond);
}
