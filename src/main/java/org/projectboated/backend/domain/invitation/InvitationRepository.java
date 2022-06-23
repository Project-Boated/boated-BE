package org.projectboated.backend.domain.invitation;

import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.invitation.entity.Invitation;
import org.projectboated.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByAccountAndProject(Account account, Project project);
    List<Invitation> findByAccount(Account account);
}
