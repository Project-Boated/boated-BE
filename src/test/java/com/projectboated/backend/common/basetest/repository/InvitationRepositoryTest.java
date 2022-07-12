package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.domain.invitation.repository.InvitationRepository;
import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;

public class InvitationRepositoryTest extends KanbanLaneRepositoryTest {

    @Autowired
    protected InvitationRepository invitationRepository;

    protected Invitation insertInvitation(Project project, Account account) {
        return invitationRepository.save(Invitation.builder()
                .project(project)
                .account(account)
                .build());
    }

}
