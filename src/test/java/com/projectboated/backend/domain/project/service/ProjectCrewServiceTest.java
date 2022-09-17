package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.utils.basetest.ServiceTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
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
import java.util.stream.IntStream;

import static com.projectboated.backend.utils.data.BasicDataAccount.*;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Project(crew) : Service 단위 테스트")
class ProjectCrewServiceTest extends ServiceTest {

    @InjectMocks
    ProjectCrewService projectCrewService;

    @Mock
    ProjectService projectService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountProjectRepository accountProjectRepository;
    @Mock
    ProjectRepository projectRepository;

    @Test
    void findAllCrews_projectId찾을수없음_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectCrewService.findAllCrews(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void findAllCrews_crew가요청_return_crews() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        List<Account> crews = IntStream.range(1, 5)
                .mapToObj(i -> Account.builder()
                        .id(ACCOUNT_ID + i)
                        .build()).toList();

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(accountProjectRepository.findCrewByProject(project)).thenReturn(crews);

        // When
        List<Account> results = projectCrewService.findAllCrews(project.getId());

        // Then
        assertThat(results)
                .extracting("id")
                .containsExactly(ACCOUNT_ID + 1, ACCOUNT_ID + 2, ACCOUNT_ID + 3, ACCOUNT_ID + 4);
    }

    @Test
    void deleteCrew_project를찾을수없음_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectCrewService.deleteCrew(PROJECT_ID, NICKNAME))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void deleteCrew_nickname으로crew를못찾을때_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Account crew = createAccount(ACCOUNT_ID2);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(accountRepository.findByNickname(crew.getNickname())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectCrewService.deleteCrew(project.getId(), crew.getNickname()))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void deleteCrew_정상Request_정상delete() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Account crew = createAccount(ACCOUNT_ID2);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(accountRepository.findByNickname(crew.getNickname())).thenReturn(Optional.of(crew));

        // When
        projectCrewService.deleteCrew(project.getId(), crew.getNickname());

        // Then
        verify(accountProjectRepository).deleteByProjectAndAccount(project, crew);
    }

}