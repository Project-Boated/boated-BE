package com.projectboated.backend.domain.invitation.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.domain.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Invitation : Persistence 단위 테스트")
class InvitationRepositoryTest extends RepositoryTest {

    @Test
    void findByAccountAndProject_account에project존재_return_AccountProject() {
        // Given
        Project project = insertProjectAndCaptain(USERNAME, NICKNAME);
        Account account2 = insertAccount(USERNAME2, NICKNAME2);
        insertInvitation(project, account2);

        // When
        Optional<Invitation> result = invitationRepository.findByAccountAndProject(account2, project);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getProject()).isEqualTo(project);
        assertThat(result.get().getAccount()).isEqualTo(account2);
    }

    @Test
    void findByAccountAndProject_account에다른project존재_return_empty() {
        // Given
        Account account = insertAccount(USERNAME, NICKNAME);
        Project project = insertProject(account);
        insertInvitation(project, account);

        Account account2 = insertAccount(USERNAME2, NICKNAME2);
        Project project2 = insertProject(account);
        insertInvitation(project2, account2);

        // When
        Optional<Invitation> result = invitationRepository.findByAccountAndProject(account, project2);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByAccount_invitation1개존재_return_1개() {
        // Given
        Account account = insertAccount(USERNAME, NICKNAME);
        Project project = insertProject(account);
        insertInvitation(project, account);

        // When
        List<Invitation> result = invitationRepository.findByAccount(account);

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void findByIdAndAccount_invitation1개존재_return_1개() {
        // Given
        Account account = insertAccount(USERNAME, NICKNAME);
        Project project = insertProject(account);
        Invitation invitation = insertInvitation(project, account);

        // When
        Optional<Invitation> result = invitationRepository.findByIdAndAccount(invitation.getId(), account);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(invitation);
    }

}