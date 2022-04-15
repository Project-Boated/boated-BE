package my.sleepydeveloper.projectcompass.domain.project.service;

import my.sleepydeveloper.projectcompass.common.basetest.UnitTest;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.accountproject.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNotFoundException;
import my.sleepydeveloper.projectcompass.domain.accountproject.repository.AccountProjectRepository;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNotFoundException;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectUpdateAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.exception.SameProjectNameInAccountExists;
import my.sleepydeveloper.projectcompass.domain.project.exception.UpdateCaptainAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import my.sleepydeveloper.projectcompass.domain.project.vo.ProjectUpdateCondition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.projectDescription;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.projectName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.context.annotation.ComponentScan.Filter;

@DataJpaTest(
        includeFilters = {
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ProjectService.class),
                @Filter(type = FilterType.ANNOTATION, classes = Repository.class)
        }
)
class ProjectServiceTest extends UnitTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountProjectRepository accountProjectRepository;

    @Test
    void save_project하나저장_저장성공() throws Exception {
        // Given
        Account account = createAccount();

        Project project = new Project(projectName, projectDescription, account);

        // When
        Project saveProject = projectService.save(project);

        // Then
        assertThat(saveProject.getName()).isEqualTo(project.getName());
        assertThat(saveProject.getDescription()).isEqualTo(project.getDescription());
        assertThat(saveProject.getCaptain().getUsername()).isEqualTo(project.getCaptain().getUsername());
    }

    @Test
    void save_이미존재하는Name으로생성_오류발생() throws Exception {
        // Given
        Account account = createAccount();

        Project project = new Project(projectName, projectDescription, account);
        projectRepository.save(project);

        // When
        // Then
        assertThatThrownBy(() -> projectService.save(new Project(projectName, "newDescription", account)))
                .isInstanceOf(SameProjectNameInAccountExists.class);
    }

    @Test
    void update_모든정보Update_업데이트성공() throws Exception {
        // Given
        Account account = createAccount();

        Project project = new Project(projectName, projectDescription, account);
        projectRepository.save(project);

        // When
        String newProjectName = "newProjectName";
        String newProjectDescription = "newProjectDescription";
        projectService.update(account, project.getId(), new ProjectUpdateCondition(newProjectName, newProjectDescription));

        // Then
        assertThat(project.getName()).isEqualTo(newProjectName);
        assertThat(project.getDescription()).isEqualTo(newProjectDescription);
    }

    @Test
    void update_같은Account에같은Name존재_오류발생() throws Exception {
        // Given
        Account account = createAccount();

        Project project = new Project(projectName, projectDescription, account);
        projectRepository.save(project);

        Project project2 = new Project("projectName2", "projectDescription2", account);
        projectRepository.save(project2);

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account, project2.getId(), new ProjectUpdateCondition(projectName, projectDescription)))
                .isInstanceOf(SameProjectNameInAccountExists.class);
    }

    @Test
    void update_존재하지않는projectId_오류발생() throws Exception {
        // Given
        Account account = createAccount();

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account, 123L, new ProjectUpdateCondition(projectName, projectDescription)))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void update_captain이아닌Account가요청_오류발생() throws Exception {
        // Given
        Account account = createAccount();
        Account account2 = new Account("username2", password, "nickname2", "ROLE_USER");
        accountRepository.save(account2);

        Project project = new Project(projectName, projectDescription, account);
        projectRepository.save(project);

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account2, project.getId(), new ProjectUpdateCondition("newProjectName", "newProjectDescription")))
                .isInstanceOf(ProjectUpdateAccessDenied.class);
    }

    @Test
    void findAllByAccount_Account의Project가없을때_빈리스트() throws Exception {
        // Given
        Account account = createAccount();

        // When
        // Then
        assertThat(projectService.findAllByAccount(account)).isEmpty();
    }

    @Test
    void findAllByAccount_Account의Project가10개존재_리스트안에10개() throws Exception {
        // Given
        Account account = createAccount();
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Project project = new Project(projectName + i, projectDescription, account);
            projectRepository.save(project);
            projects.add(project);
        }

        // When
        int result = projectService.findAllByAccount(account).size();

        // Then
        assertThat(result).isEqualTo(projects.size());
    }

    @Test
    void findAllCrew_한명의crew가있을때_한명의crew조회() throws Exception {
        // Given
        Account captain = createAccount();
        Project project = new Project(projectName, projectDescription, captain);
        projectRepository.save(project);

        Account crew = new Account("crew", password, "crew", "ROLE_USER");
        accountRepository.save(crew);

        AccountProject accountProject = new AccountProject(crew, project);
        accountProjectRepository.save(accountProject);

        // When
        List<Account> crews = projectService.findAllCrews(project.getId());

        // Then
        assertThat(crews.size()).isEqualTo(1);
    }

    @Test
    void findAllCrew_존재하지않는ProjectId_오류발생() throws Exception {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> projectService.findAllCrews(1L))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void updateCaptain_새로운captain으로업데이트_업데이트성공() throws Exception {
        // Given
        String newUsername = "newUsername";
        String newNickname = "newNickname";
        String newPassword = "newPassword";
        Account newCaptain = new Account(newUsername, newPassword, newNickname, "ROLE_USER");
        accountRepository.save(newCaptain);
        Account captain = createAccount();
        accountRepository.save(captain);

        Project project = new Project(projectName, projectDescription, captain);
        projectRepository.save(project);

        accountProjectRepository.save(new AccountProject(newCaptain, project));

        // When
        projectService.updateCaptain(captain, newCaptain.getUsername(), project.getId());

        // Then
        assertThat(project.getCaptain().getId()).isEqualTo(newCaptain.getId());
    }

    @Test
    void updateCaptain_존재하지않는projectId_오류발생() throws Exception {
        // Given
        String newUsername = "newUsername";
        String newNickname = "newNickname";
        String newPassword = "newPassword";
        Account newCaptain = new Account(newUsername, newPassword, newNickname, "ROLE_USER");
        accountRepository.save(newCaptain);
        Account captain = createAccount();
        accountRepository.save(captain);

        Project project = new Project(projectName, projectDescription, captain);
        projectRepository.save(project);

        accountProjectRepository.save(new AccountProject(newCaptain, project));

        // When
        // Then
        assertThatThrownBy(() -> projectService.updateCaptain(captain, newCaptain.getUsername(), 2192L))
                .isInstanceOf(ProjectNotFoundException.class);
    }


    @Test
    void updateCaptain_captain이아닌account가update_오류발생() throws Exception {
        // Given
        String newUsername = "newUsername";
        String newNickname = "newNickname";
        String newPassword = "newPassword";
        Account newCaptain = new Account(newUsername, newPassword, newNickname, "ROLE_USER");
        accountRepository.save(newCaptain);
        Account captain = createAccount();
        accountRepository.save(captain);

        Project project = new Project(projectName, projectDescription, captain);
        projectRepository.save(project);

        accountProjectRepository.save(new AccountProject(newCaptain, project));

        // When
        // Then
        assertThatThrownBy(() -> projectService.updateCaptain(newCaptain, newCaptain.getUsername(), project.getId()))
                .isInstanceOf(UpdateCaptainAccessDenied.class);
    }

    @Test
    void updateCaptain_새로운Captain의username을찾을수없음_오류발생() throws Exception {
        // Given
        String newUsername = "newUsername";
        String newNickname = "newNickname";
        String newPassword = "newPassword";
        Account newCaptain = new Account(newUsername, newPassword, newNickname, "ROLE_USER");
        accountRepository.save(newCaptain);
        Account captain = createAccount();
        accountRepository.save(captain);

        Project project = new Project(projectName, projectDescription, captain);
        projectRepository.save(project);

        accountProjectRepository.save(new AccountProject(newCaptain, project));

        // When
        // Then
        assertThatThrownBy(() -> projectService.updateCaptain(captain, "fail", project.getId()))
                .isInstanceOf(AccountNotFoundException.class);
    }


    private Account createAccount() {
        Account account = new Account(username, password, nickname, "ROLE_USER");
        accountRepository.save(account);
        return account;
    }
}