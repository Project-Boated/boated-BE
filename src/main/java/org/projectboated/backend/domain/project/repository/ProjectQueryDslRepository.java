package org.projectboated.backend.domain.project.repository;

import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.project.condition.GetMyProjectsCond;
import org.projectboated.backend.domain.project.entity.Project;

import java.util.List;

public interface ProjectQueryDslRepository {

    List<Project> getMyProjects(Account account, GetMyProjectsCond cond);
}
