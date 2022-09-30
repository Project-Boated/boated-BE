package com.projectboated.backend.utils.base.base;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.invitation.entity.Invitation;
import com.projectboated.backend.project.project.entity.Project;

import java.time.LocalDateTime;

public class BaseInvitationTest extends BaseTaskTest {

    protected Invitation createInvitation(Project project, Account account) {
        Invitation invitation = Invitation.builder()
                .account(account)
                .project(project)
                .build();
        invitation.changeCreatedDate(LocalDateTime.now());
        return invitation;
    }

    protected Invitation createInvitation(Long id, Project project, Account account) {
        Invitation invitation = Invitation.builder()
                .id(id)
                .account(account)
                .project(project)
                .build();
        invitation.changeCreatedDate(LocalDateTime.now());
        return invitation;
    }

}
