package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectFindAllCrewsAccessDeniedException;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID2;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectCrewServiceTest extends ServiceTest {

    @InjectMocks
    ProjectCrewService projectCrewService;

    @Mock
    AccountProjectService accountProjectService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountProjectRepository accountProjectRepository;
    @Mock
    ProjectRepository projectRepository;

    @Test
    void findAllCrews_accountId찾을수없음_예외발생() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectCrewService.findAllCrews(account.getId(), PROJECT_ID))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void findAllCrews_projectId찾을수없음_예외발생() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectCrewService.findAllCrews(account.getId(), PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void findAllCrews_captain아님crew도아님_예외발생() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .build();

        Project project = Project.builder()
                .id(PROJECT_ID)
                .captain(captain)
                .build();

        Account requestAccount = Account.builder()
                .id(ACCOUNT_ID2)
                .build();

        when(accountRepository.findById(requestAccount.getId())).thenReturn(Optional.of(requestAccount));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(accountProjectService.isCrew(project, requestAccount)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> projectCrewService.findAllCrews(requestAccount.getId(), project.getId()))
                .isInstanceOf(ProjectFindAllCrewsAccessDeniedException.class);
    }

    @Test
    void findAllCrews_captain이요청_return_crews() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .build();

        Project project = Project.builder()
                .id(PROJECT_ID)
                .captain(captain)
                .build();

        List<Account> crews = IntStream.range(1, 5)
                .mapToObj(i -> Account.builder()
                        .id(ACCOUNT_ID + i)
                        .build()).toList();

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(accountProjectRepository.findCrewByProject(project)).thenReturn(crews);

        // When
        List<Account> results = projectCrewService.findAllCrews(captain.getId(), project.getId());

        // Then
        assertThat(results)
                .extracting("id")
                .containsExactly(ACCOUNT_ID + 1, ACCOUNT_ID + 2, ACCOUNT_ID + 3, ACCOUNT_ID + 4);
    }

    @Test
    void findAllCrews_crew가요청_return_crews() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .build();

        Project project = Project.builder()
                .id(PROJECT_ID)
                .captain(captain)
                .build();

        Account crew = Account.builder()
                .id(ACCOUNT_ID2)
                .build();

        List<Account> crews = IntStream.range(1, 5)
                .mapToObj(i -> Account.builder()
                        .id(ACCOUNT_ID + i)
                        .build()).toList();

        when(accountRepository.findById(crew.getId())).thenReturn(Optional.of(crew));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(accountProjectService.isCrew(project, crew)).thenReturn(true);
        when(accountProjectRepository.findCrewByProject(project)).thenReturn(crews);

        // When
        List<Account> results = projectCrewService.findAllCrews(crew.getId(), project.getId());

        // Then
        assertThat(results)
                .extracting("id")
                .containsExactly(ACCOUNT_ID + 1, ACCOUNT_ID + 2, ACCOUNT_ID + 3, ACCOUNT_ID + 4);
    }

}