package com.projectboated.backend.domain.invitation.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByAccountAndProject(Account account, Project project);
    List<Invitation> findByAccount(Account account);

    Optional<Invitation> findByIdAndAccount(Long id, Account account);
}
