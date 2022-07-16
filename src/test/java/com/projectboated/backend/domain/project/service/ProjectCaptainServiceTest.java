package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectCaptainUpdateAccessDeniedException;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataAccount.PASSWORD;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProjectCaptainServiceTest extends ServiceTest {

    @InjectMocks
    ProjectCaptainService projectCaptainService;

    @Mock
    AccountProjectService accountProjectService;

    @Mock
    ProjectRepository projectRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountProjectRepository accountProjectRepository;

    @Test
    void updateCaptain_새로운captain으로업데이트_업데이트성공() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();
        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Account crew = Account.builder()
                .id(ACCOUNT_ID2)
                .username(USERNAME2)
                .nickname(NICKNAME2)
                .password(PASSWORD2)
                .build();

        when(projectRepository.findById(any())).thenReturn(Optional.of(project));
        when(accountRepository.findByNickname(any())).thenReturn(Optional.of(crew));
        when(accountProjectService.isCrew(any(), any())).thenReturn(true);

        // When
        projectCaptainService.updateCaptain(captain, project.getId(), crew.getUsername());

        // Then
        assertThat(project.getCaptain()).isEqualTo(crew);

        verify(accountProjectRepository).save(any());
        verify(accountProjectRepository).deleteByProjectAndAccount(any(), any());
    }

    @Test
    void updateCaptain_존재하지않는projectId_오류발생() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();
        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Account crew = Account.builder()
                .id(ACCOUNT_ID2)
                .username(USERNAME2)
                .nickname(NICKNAME2)
                .password(PASSWORD2)
                .build();

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectCaptainService.updateCaptain(captain, PROJECT_ID, crew.getNickname()))
                .isInstanceOf(ProjectNotFoundException.class);
    }


    @Test
    void updateCaptain_captain이아닌account가update_오류발생() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();
        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Account crew = Account.builder()
                .id(ACCOUNT_ID2)
                .username(USERNAME2)
                .nickname(NICKNAME2)
                .password(PASSWORD2)
                .build();

        when(projectRepository.findById(any())).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> projectCaptainService.updateCaptain(crew, project.getId(), crew.getUsername()))
                .isInstanceOf(ProjectCaptainUpdateAccessDeniedException.class);
    }

    @Test
    void updateCaptain_새로운Captain의username을찾을수없음_오류발생() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();
        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Account crew = Account.builder()
                .id(ACCOUNT_ID2)
                .username(USERNAME2)
                .nickname(NICKNAME2)
                .password(PASSWORD2)
                .build();

        when(projectRepository.findById(any())).thenReturn(Optional.of(project));
        when(accountRepository.findByNickname(crew.getNickname())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectCaptainService.updateCaptain(captain, project.getId(), crew.getNickname()))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void updateCaptain_Update할accountcrew가아님_오류발생() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();
        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Account crew = Account.builder()
                .id(ACCOUNT_ID2)
                .username(USERNAME2)
                .nickname(NICKNAME2)
                .password(PASSWORD2)
                .build();

        when(projectRepository.findById(any())).thenReturn(Optional.of(project));
        when(accountRepository.findByNickname(crew.getNickname())).thenReturn(Optional.of(crew));
        when(accountProjectService.isCrew(any(), any())).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> projectCaptainService.updateCaptain(captain, project.getId(), crew.getNickname()))
                .isInstanceOf(ProjectCaptainUpdateAccessDeniedException.class);
    }

}