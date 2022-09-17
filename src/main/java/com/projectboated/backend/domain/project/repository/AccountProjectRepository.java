package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountProjectRepository extends JpaRepository<AccountProject, Long>, AccountProjectQueryDslRepository {

    @Modifying
    @Query("delete from AccountProject ap where ap.project=:project and ap.account=:account")
    void deleteByProjectAndAccount(@Param("project") Project project, @Param("account") Account account);

    @Query("select count(ap) from AccountProject ap where ap.account=:crew and ap.project=:project")
    Long countByCrewInProject(@Param("crew") Account crew, @Param("project") Project project);

    Optional<AccountProject> findByAccountAndProject(Account account, Project project);

    List<AccountProject> findByAccount(Account account);
}
