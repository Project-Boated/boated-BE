package com.projectboated.backend.project.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.entity.Project;

import java.util.List;

public interface AccountProjectQueryDslRepository {

    List<Account> findCrewByProject(Project project);
}
