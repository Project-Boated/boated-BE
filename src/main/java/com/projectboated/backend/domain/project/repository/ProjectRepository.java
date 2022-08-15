package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectQueryDslRepository {

    boolean existsByNameAndCaptain(String name, Account captain);

}
