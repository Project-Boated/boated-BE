package com.projectboated.backend.domain.invitation.entity;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Invitation : Entity 단위 테스트")
class InvitationTest extends RepositoryTest {

    @Test
    void 생성자_Invitation생성_return_생성된Invitation() {
        // Given
        Account account = Account.builder()
                .build();
        Project project = Project.builder()
                .build();

        // When
        Invitation invitation = new Invitation(account, project);

        // Then
        assertThat(invitation.getAccount()).isEqualTo(account);
        assertThat(invitation.getProject()).isEqualTo(project);
    }

}