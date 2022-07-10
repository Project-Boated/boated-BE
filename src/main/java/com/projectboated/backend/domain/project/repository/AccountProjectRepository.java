package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountProjectRepository extends JpaRepository<AccountProject, Long>, AccountProjectQueryDslRepository {

    @Modifying
    @Query("delete from AccountProject ap where ap.project=:project and ap.account=:account")
    void deleteByProjectAndAccount(@Param("project") Project project, @Param("account") Account account);

    @Query("select count(ap) from AccountProject ap where ap.project.id=:projectId and ap.account.id=:accountId")
    Long countByProjectIdAndAccountId(@Param("projectId") long projectId, @Param("accountId") long accountId);

    @Query("select count(ap) from AccountProject ap where ap.account.id=:crewId and ap.project.id=:projectId")
    Long countByCrewInProject(@Param("crewId") Long crewId, @Param("projectId") Long projectId);
}
