package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.domain.project.entity.Project;

import java.time.LocalDateTime;

import static com.projectboated.backend.common.data.BasicDataInvitation.INVITATION_ID;

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
