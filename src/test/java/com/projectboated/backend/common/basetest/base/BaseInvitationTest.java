package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.domain.project.entity.Project;

public class BaseInvitationTest extends BaseTaskTest {

    protected Invitation createInvitation(Project project, Account account) {
        return Invitation.builder()
                .account(account)
                .project(project)
                .build();
    }

}
