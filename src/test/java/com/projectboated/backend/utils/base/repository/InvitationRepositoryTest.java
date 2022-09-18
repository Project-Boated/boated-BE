package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.invitation.entity.Invitation;
import com.projectboated.backend.invitation.repository.InvitationRepository;
import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;

public class InvitationRepositoryTest extends KanbanLaneRepositoryTest {

    @Autowired
    protected InvitationRepository invitationRepository;

    protected Invitation insertInvitation(Project project, Account account) {
        return invitationRepository.save(createInvitation(project, account));
    }

}
