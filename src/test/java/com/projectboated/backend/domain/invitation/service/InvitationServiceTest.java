package com.projectboated.backend.domain.invitation.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.invitation.entity.Invitation;
import com.projectboated.backend.domain.invitation.repository.InvitationRepository;
import com.projectboated.backend.domain.invitation.service.exception.InvitationExistsAccount;
import com.projectboated.backend.domain.invitation.service.exception.InvitationNotFoundException;
import com.projectboated.backend.domain.invitation.service.exception.InviteCanNotInviteCaptain;
import com.projectboated.backend.domain.invitation.service.exception.InviteCanNotInviteCrew;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataInvitation.INVITATION_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Invitation : Service 단위 테스트")
class InvitationServiceTest extends ServiceTest {

    @InjectMocks
    InvitationService invitationService;

    @Mock
    ProjectRepository projectRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountProjectRepository accountProjectRepository;
    @Mock
    InvitationRepository invitationRepository;

    @Test
    void inviteCrew_찾을수없는프로젝트_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(PROJECT_ID, NICKNAME))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void inviteCrew_찾을수없는Account_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(accountRepository.findByNickname(NICKNAME)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(PROJECT_ID, NICKNAME))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void inviteCrew_Captain을Invite_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(accountRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(captain));

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(PROJECT_ID, NICKNAME))
                .isInstanceOf(InviteCanNotInviteCaptain.class);
    }

    @Test
    void inviteCrew_Crew을Invite_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Account crew = createAccount(ACCOUNT_ID2);
        AccountProject accountProject = createAccountProject(crew, project);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(accountRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(crew));
        when(accountProjectRepository.findByAccountAndProject(crew, project)).thenReturn(Optional.of(accountProject));

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(PROJECT_ID, NICKNAME))
                .isInstanceOf(InviteCanNotInviteCrew.class);
    }

    @Test
    void inviteCrew_이미초대된경우_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Account crew = createAccount(ACCOUNT_ID2);
        Invitation invitation = createInvitation(project, crew);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(accountRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(crew));
        when(accountProjectRepository.findByAccountAndProject(crew, project)).thenReturn(Optional.empty());
        when(invitationRepository.findByAccountAndProject(crew, project)).thenReturn(Optional.of(invitation));

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(PROJECT_ID, NICKNAME))
                .isInstanceOf(InvitationExistsAccount.class);
    }

    @Test
    void inviteCrew_정상요청_invite성공() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Account crew = createAccount(ACCOUNT_ID2);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(accountRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(crew));
        when(accountProjectRepository.findByAccountAndProject(crew, project)).thenReturn(Optional.empty());
        when(invitationRepository.findByAccountAndProject(crew, project)).thenReturn(Optional.empty());

        // When
        invitationService.inviteCrew(PROJECT_ID, NICKNAME);

        // Then
        verify(invitationRepository).save(any());
    }

    @Test
    void findByAccount_정상요청_return_Invitation_list() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Invitation invitation = createInvitation(project, captain);

        when(invitationRepository.findByAccount(captain)).thenReturn(List.of(invitation));

        // When
        List<Invitation> result = invitationService.findByAccount(captain);

        // Then
        assertThat(result).contains(invitation);
    }

    @Test
    void accept_account가없는경우_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> invitationService.accept(ACCOUNT_ID, INVITATION_ID))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void accept_invitation이없는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(invitationRepository.findByIdAndAccount(INVITATION_ID, account)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> invitationService.accept(ACCOUNT_ID, INVITATION_ID))
                .isInstanceOf(InvitationNotFoundException.class);
    }

    @Test
    void accept_정상요청_accept성공() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Invitation invitation = createInvitation(project, account);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(invitationRepository.findByIdAndAccount(INVITATION_ID, account)).thenReturn(Optional.of(invitation));

        // When
        Long result = invitationService.accept(ACCOUNT_ID, INVITATION_ID);

        // Then
        assertThat(result).isEqualTo(project.getId());

        verify(accountProjectRepository).save(any());
        verify(invitationRepository).delete(any());
    }

    @Test
    void reject_찾을수없는account_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> invitationService.reject(ACCOUNT_ID, INVITATION_ID))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void reject_찾을수없는Invitation_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(invitationRepository.findByIdAndAccount(INVITATION_ID, account)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> invitationService.reject(ACCOUNT_ID, INVITATION_ID))
                .isInstanceOf(InvitationNotFoundException.class);
    }

    @Test
    void reject_정상요청_reject성공() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(account);
        Invitation invitation = createInvitation(project, account);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(invitationRepository.findByIdAndAccount(INVITATION_ID, account)).thenReturn(Optional.of(invitation));

        // When
        invitationService.reject(ACCOUNT_ID, INVITATION_ID);

        // Then
        verify(invitationRepository).delete(invitation);
    }
}