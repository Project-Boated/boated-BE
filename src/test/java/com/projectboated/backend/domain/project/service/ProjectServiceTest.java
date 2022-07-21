package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.condition.GetMyProjectsCond;
import com.projectboated.backend.domain.project.service.condition.ProjectUpdateCond;
import com.projectboated.backend.domain.project.service.dto.MyProjectsDto;
import com.projectboated.backend.domain.project.service.exception.ProjectDeleteAccessDeniedException;
import com.projectboated.backend.domain.project.service.exception.ProjectNameSameInAccountException;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.project.service.exception.ProjectUpdateAccessDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Project : Service 단위 테스트")
class ProjectServiceTest extends ServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;
    @Mock
    AccountRepository accountRepository;

    @Test
    void save_project저장_성공() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        Project project = Project.builder()
                .captain(account)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        when(projectRepository.existsByNameAndCaptain(PROJECT_NAME, account)).thenReturn(false);
        when(projectRepository.save(project)).thenReturn(project);

        // When
        Project saveProject = projectService.save(project);

        // Then
        assertThat(saveProject.getName()).isEqualTo(project.getName());
        assertThat(saveProject.getDescription()).isEqualTo(project.getDescription());
        assertThat(saveProject.getCaptain().getId()).isEqualTo(project.getCaptain().getId());
        assertThat(saveProject.getKanban()).isNotNull();
        assertThat(saveProject.getKanban().getLanes()).hasSize(4);
    }

    @Test
    void save_captain이가진Name으로생성_오류발생() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        Project project = Project.builder()
                .captain(account)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        when(projectRepository.existsByNameAndCaptain(PROJECT_NAME, account)).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> projectService.save(project))
                .isInstanceOf(ProjectNameSameInAccountException.class);
    }

    @Test
    void update_모든정보Update_업데이트성공() {
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

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectRepository.existsByNameAndCaptain(any(), any())).thenReturn(false);

        // When
        String newProjectName = "newProjectName";
        String newProjectDescription = "newProjectDescription";
        LocalDateTime newDeadline = PROJECT_DEADLINE.plus(1, ChronoUnit.DAYS);
        projectService.update(captain.getId(), PROJECT_ID, new ProjectUpdateCond(newProjectName, newProjectDescription, newDeadline));

        // Then
        assertThat(project.getName()).isEqualTo(newProjectName);
        assertThat(project.getDescription()).isEqualTo(newProjectDescription);
        assertThat(project.getDeadline()).isEqualTo(newDeadline);
    }

    @Test
    void update_모든정보NULL_업데이트안됨() {
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

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        projectService.update(captain.getId(), PROJECT_ID, new ProjectUpdateCond(null, null, null));

        // Then
        assertThat(project.getName()).isEqualTo(PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(PROJECT_DESCRIPTION);
        assertThat(project.getDeadline()).isEqualTo(PROJECT_DEADLINE);
    }

    @Test
    void update_존재하지않는projectId_오류발생() {
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

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(captain.getId(), PROJECT_ID, new ProjectUpdateCond(PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE)))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void update_찾을수없는account_예외발생() {
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

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(captain.getId(), project.getId(), new ProjectUpdateCond("newProjectName", "newProjectDescription", null)))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void update_captain이아닌Account가요청_오류발생() {
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

        when(accountRepository.findById(crew.getId())).thenReturn(Optional.of(crew));
        when(projectRepository.findById(any())).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(crew.getId(), project.getId(), new ProjectUpdateCond("newProjectName", "newProjectDescription", null)))
                .isInstanceOf(ProjectUpdateAccessDeniedException.class);
    }

    @Test
    void update_같은Account에같은Name존재_오류발생() {
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

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectRepository.existsByNameAndCaptain(any(), any())).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(captain.getId(), PROJECT_ID, new ProjectUpdateCond(PROJECT_NAME2, PROJECT_DESCRIPTION2, PROJECT_DEADLINE2)))
                .isInstanceOf(ProjectNameSameInAccountException.class);
    }

    @Test
    void findById_존재하는Id_return_project() {
        // Given
        Project project = Project.builder()
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        Project byId = projectService.findById(PROJECT_ID);

        // Then
        assertThat(byId.getName()).isEqualTo(project.getName());
        assertThat(byId.getDescription()).isEqualTo(project.getDescription());
        assertThat(byId.getDeadline()).isEqualTo(project.getDeadline());
    }

    @Test
    void findById_존재하지않는ProjectId_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.findById(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void delete_존재하지않는AccountId_예외발생() {
        // Given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.delete(ACCOUNT_ID, 123L))
                .isInstanceOf(AccountNotFoundException.class);
    }


    @Test
    void delete_ProjectId로찾을수없음_예외발생() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.delete(captain.getId(), PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void delete_captain이아님_예외발생() {
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

        when(accountRepository.findById(crew.getId())).thenReturn(Optional.of(crew));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> projectService.delete(crew.getId(), PROJECT_ID))
                .isInstanceOf(ProjectDeleteAccessDeniedException.class);
    }

    @Test
    void delete_존재하는projectId와captain이요청_삭제() {
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

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        projectService.delete(captain.getId(), PROJECT_ID);

        // Then
        verify(projectRepository).delete(any());
    }

    @Test
    void getMyProjects_찾을수없는accountId_예외발생() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .captainTerm(true)
                .pageable(PageRequest.of(1, 10))
                .build();

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.getMyProjects(captain.getId(), cond))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getMyProjects_내project조회_정상() {
        // Given
        Account captain = Account.builder()
                .id(ACCOUNT_ID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .captainTerm(true)
                .pageable(PageRequest.of(1, 10))
                .build();

        List<Project> projects = List.of(Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build());

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.getMyProjects(captain, cond)).thenReturn(projects);

        // When
        MyProjectsDto result = projectService.getMyProjects(captain.getId(), cond);

        // Then
        assertThat(result.getProjects()).isEqualTo(projects);
        assertThat(result.getPage()).isEqualTo(cond.getPageable().getPageNumber());
        assertThat(result.getSize()).isEqualTo(result.getProjects().size());
    }

}