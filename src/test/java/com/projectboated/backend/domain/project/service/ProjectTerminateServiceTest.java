package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.project.service.exception.ProjectTerminateAccessDeniedException;
import com.projectboated.backend.domain.project.service.exception.ProjectUpdateAccessDeniedException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID2;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProjectTerminateServiceTest extends ServiceTest {

    @InjectMocks
    ProjectTerminateService projectTerminateService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    ProjectRepository projectRepository;
    
    @Test
    void terminateProject_accountId찾을수없음_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        
        // When
        // Then
        assertThatThrownBy(() -> projectTerminateService.terminateProject(ACCOUNT_ID, PROJECT_ID))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void terminateProject_projectId찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectTerminateService.terminateProject(ACCOUNT_ID, PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void terminateProject_captain이아닐경우_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Account account = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID2)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> projectTerminateService.terminateProject(ACCOUNT_ID2, PROJECT_ID))
                .isInstanceOf(ProjectTerminateAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID2);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void terminateProject_정상request_terminate정상() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        projectTerminateService.terminateProject(ACCOUNT_ID, PROJECT_ID);

        // Then
        assertThat(project.isTerminated()).isTrue();

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void cancelTerminateProject_accountId찾을수없음_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectTerminateService.cancelTerminateProject(ACCOUNT_ID, PROJECT_ID))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
    }

    @Test
    void cancelTerminateProject_projectId찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectTerminateService.cancelTerminateProject(ACCOUNT_ID, PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void cancelTerminateProject_captain이아닐경우_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);
        Account account = createAccount(ACCOUNT_ID2);

        when(accountRepository.findById(ACCOUNT_ID2)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> projectTerminateService.cancelTerminateProject(ACCOUNT_ID2, PROJECT_ID))
                .isInstanceOf(ProjectUpdateAccessDeniedException.class);

        verify(accountRepository).findById(ACCOUNT_ID2);
        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void cancelTerminateProject_정상request_terminate정상() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(captain);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        projectTerminateService.cancelTerminateProject(ACCOUNT_ID, PROJECT_ID);

        // Then
        assertThat(project.isTerminated()).isFalse();

        verify(accountRepository).findById(ACCOUNT_ID);
        verify(projectRepository).findById(PROJECT_ID);
    }


}