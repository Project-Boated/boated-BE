package com.projectboated.backend.invitation.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.invitation.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByAccountAndProject(Account account, Project project);

    List<Invitation> findByAccount(Account account);

    @Query("select i from Invitation i where i.id=:id and i.account.id=:accountId")
    Optional<Invitation> findByIdAndAccountId(@Param("id") Long id, @Param("accountId") Long accountId);
}
