package com.projectboated.backend.project.project.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.service.condition.GetMyProjectsCond;

import java.util.List;

public interface ProjectQueryDslRepository {

    List<Project> getMyProjects(Account account, GetMyProjectsCond cond);
}
