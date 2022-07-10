package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;

import java.util.List;

public interface AccountProjectQueryDslRepository {

    List<Account> findCrewByProject(Project project);
}
