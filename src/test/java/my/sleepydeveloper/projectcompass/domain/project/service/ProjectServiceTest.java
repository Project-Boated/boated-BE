package my.sleepydeveloper.projectcompass.domain.project.service;

import my.sleepydeveloper.projectcompass.common.data.ProjectBasicData;
import my.sleepydeveloper.projectcompass.common.mock.WithMockJsonUser;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.account.exception.NotFoundAccountException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountProjectRepository;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNotFoundException;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectUpdateAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.exception.SameProjectNameInAccountExists;
import my.sleepydeveloper.projectcompass.domain.project.exception.UpdateCaptainAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import my.sleepydeveloper.projectcompass.domain.project.vo.ProjectUpdateCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static my.sleepydeveloper.projectcompass.common.data.AccountBasicData.*;
import static my.sleepydeveloper.projectcompass.common.data.ProjectBasicData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@DataJpaTest
//@Import(TestConfigRegisterAccountService.class)
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    AccountProjectRepository accountProjectRepository;

    @Test
    @DisplayName("save_성공")
    void save_성공() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, "ROLE_USER");
        Project project = new Project(projectName, projectDescription, account);

        when(projectRepository.save(project)).thenReturn(project);

        // When
        Project saveProject = projectService.save(project);

        // Then
        assertThat(saveProject.getName()).isEqualTo(project.getName());
        assertThat(saveProject.getDescription()).isEqualTo(project.getDescription());
        assertThat(saveProject.getCaptain().getUsername()).isEqualTo(project.getCaptain().getUsername());
    }

    @Test
    @DisplayName("save_실패_같은이름의_Project_존재")
    void save_실패_같은이름의_Project_존재() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, "ROLE_USER");
        Project project = new Project(projectName, projectDescription, account);

        when(projectRepository.existsByNameAndCaptain(any(), any())).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> projectService.save(project)).isInstanceOf(SameProjectNameInAccountExists.class);
    }

    @Test
    @DisplayName("update정상")
    void update_정상() throws Exception {
        // Given
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        Project project = new Project(projectName, projectDescription, account);

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(projectRepository.existsByNameAndCaptain(any(), any())).thenReturn(false);
        when(projectRepository.findById(any())).thenReturn(Optional.of(project));

        // When
        // Then
        projectService.update(account.getId(), project.getId(), new ProjectUpdateCondition(projectName, projectDescription));
    }

    @Test
    @DisplayName("update실패_accountId로_account_찾을수없음")
    void update_실패_accountId로_account_찾을수없음() throws Exception {
        // Given
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        Project project = new Project(projectName, projectDescription, account);

        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account.getId(), project.getId(), new ProjectUpdateCondition(projectName, projectDescription)))
                .isInstanceOf(NotFoundAccountException.class);
    }

    @Test
    @DisplayName("update_실패_같은account에_같은이름존재")
    void update_실패_같은_account에_같은이름존재() throws Exception {
        // Given
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        Project project = new Project(projectName, projectDescription, account);

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(projectRepository.findById(any())).thenReturn(Optional.of(project));
        when(projectRepository.existsByNameAndCaptain(any(), any())).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account.getId(), project.getId(), new ProjectUpdateCondition(projectName, projectDescription)))
                .isInstanceOf(SameProjectNameInAccountExists.class);
    }

    @Test
    @DisplayName("update_실패_projectId로_project못찾음")
    void update_실패_projectId로_project못찾음() throws Exception {
        // Given
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        Project project = new Project(projectName, projectDescription, account);

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(projectRepository.findById(any())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account.getId(), project.getId(), new ProjectUpdateCondition(projectName, projectDescription)))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    @DisplayName("update_실패_account권한없음")
    void update_실패_account권한없음() throws Exception {
        // Given
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        Account account2 = new Account(accountId + 1, username, password, nickname, "ROLE_USER");
        Project project = new Project(projectName, projectDescription, account);

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(projectRepository.findById(any())).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account2.getId(), project.getId(), new ProjectUpdateCondition(projectName, projectDescription)))
                .isInstanceOf(ProjectUpdateAccessDenied.class);
    }

    @Test
    @DisplayName("findAllByAccount_project_아무것도없을때")
    void findAllByAccount_project_아무것도없을때() throws Exception {
        // Given
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");

        // When
        when(projectService.findAllByAccount(account)).thenReturn(Collections.emptyList());

        // Then
        assertThat(projectService.findAllByAccount(account)).isEmpty();
    }

    @Test
    @DisplayName("findAllByAccount_project_10개존재")
    void findAllByAccount_project_10개존재() throws Exception {
        // Given
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            projects.add(new Project(projectName + i, projectDescription , account));
        }

        // When
        when(projectService.findAllByAccount(account)).thenReturn(projects);

        // Then
        assertThat(projectService.findAllByAccount(account).size()).isEqualTo(10);
    }

    @Test
    @DisplayName("findAllCrew_성공")
    void findAllCrew_성공() throws Exception {
        // Given
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        Project project = new Project(projectId, projectName, projectDescription, account);

        when(projectRepository.findById(any())).thenReturn(Optional.of(project));
        when(accountProjectRepository.findCrewsFromProject(project)).thenReturn(List.of(account));

        // When
        // Then
        assertThat(projectService.findAllCrews(project.getId()))
                .element(0)
                .extracting("id", "username", "password", "nickname")
                .containsExactly(accountId, username, password, nickname);
    }

    @Test
    @DisplayName("findAllCrew_실패_projectid로_project찾기실패")
    void findAllCrew_실패_projectid로_project찾기실패() throws Exception {
        // Given
        when(projectRepository.findById(any())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.findAllCrews(1L))
                .isInstanceOf(ProjectNotFoundException.class);

    }

    @Test
    @DisplayName("updateCaptain_성공")
    void updateCaptain_성공() throws Exception {
        // Given
        String newUsername = "newUsername";
        String newNickname = "newNickname";
        String newPassword = "newPassword";
        Long newAccountId = accountId + 20;
        Account newCaptain = new Account(newAccountId, newUsername, newPassword, newNickname, "ROLE_USER");
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        Project project = new Project(projectId, projectName, projectDescription, account);
        AccountProject accountProject = new AccountProject(account, project);

        when(projectRepository.findById(any())).thenReturn(Optional.of(project));
        when(accountRepository.findByUsername(any())).thenReturn(Optional.of(newCaptain));
        when(accountProjectRepository.save(any())).thenReturn(accountProject);
        when(accountProjectRepository.delete(any(), any())).thenReturn(1L);

        // When
        projectService.updateCaptain(account, newCaptain.getUsername(), project.getId());

        // Then
        assertThat(project.getCaptain().getId()).isEqualTo(newCaptain.getId());
    }

    @Test
    @DisplayName("updateCaptain_실패_projectid로_project찾기실패")
    void updateCaptain_실패_projectid로_project찾기실패() throws Exception {
        // Given
        String newUsername = "newUsername";
        String newNickname = "newNickname";
        String newPassword = "newPassword";
        Long newAccountId = accountId + 20;
        Account newCaptain = new Account(newAccountId, newUsername, newPassword, newNickname, "ROLE_USER");
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        Project project = new Project(projectId, projectName, projectDescription, account);
        AccountProject accountProject = new AccountProject(account, project);

        when(projectRepository.findById(any())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.updateCaptain(account, newCaptain.getUsername(), project.getId()))
                .isInstanceOf(ProjectNotFoundException.class);
    }


    @Test
    @DisplayName("updateCaptain_실패_권한없음")
    void updateCaptain_실패_권한없음() throws Exception {
        // Given
        String newUsername = "newUsername";
        String newNickname = "newNickname";
        String newPassword = "newPassword";
        Long newAccountId = accountId + 20;
        Account newCaptain = new Account(newAccountId, newUsername, newPassword, newNickname, "ROLE_USER");
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        Project project = new Project(projectId, projectName, projectDescription, account);
        AccountProject accountProject = new AccountProject(account, project);

        when(projectRepository.findById(any())).thenReturn(Optional.of(project));

        // When
        // Then
        assertThatThrownBy(() -> projectService.updateCaptain(new Account(999L, username, password, nickname, "USER_ROLE"), newCaptain.getUsername(), project.getId()))
                .isInstanceOf(UpdateCaptainAccessDenied.class);
    }

    @Test
    @DisplayName("updateCaptain_실패_newCaptain_찾을수없음")
    void updateCaptain_실패_newCaptain_찾을수없음() throws Exception {
        // Given
        String newUsername = "newUsername";
        String newNickname = "newNickname";
        String newPassword = "newPassword";
        Long newAccountId = accountId + 20;
        Account newCaptain = new Account(newAccountId, newUsername, newPassword, newNickname, "ROLE_USER");
        Account account = new Account(accountId, username, password, nickname, "ROLE_USER");
        Project project = new Project(projectId, projectName, projectDescription, account);
        AccountProject accountProject = new AccountProject(account, project);

        when(projectRepository.findById(any())).thenReturn(Optional.of(project));
        when(accountRepository.findByUsername(any())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.updateCaptain(account, newCaptain.getUsername(), project.getId()))
                .isInstanceOf(NotFoundAccountException.class);
    }

}