package my.sleepydeveloper.projectcompass.domain.project.service;

import my.sleepydeveloper.projectcompass.aws.AwsS3Service;
import my.sleepydeveloper.projectcompass.common.basetest.BaseTest;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNotFoundException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.domain.accountproject.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.accountproject.repository.AccountProjectRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNotFoundException;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectUpdateAccessDeniedException;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNameSameInAccountException;
import my.sleepydeveloper.projectcompass.domain.project.exception.UpdateCaptainAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import my.sleepydeveloper.projectcompass.domain.project.vo.ProjectUpdateCondition;
import my.sleepydeveloper.projectcompass.domain.uploadfile.repository.UploadFileRepository;
import my.sleepydeveloper.projectcompass.security.service.KakaoWebService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@Disabled
class ProjectServiceTest extends BaseTest {

    @Autowired
    AwsS3Service awsS3Service;

    @Autowired
    KakaoWebService kakaoWebService;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    UploadFileRepository uploadFileRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountProjectRepository accountProjectRepository;

    @Test
    void save_project저장_성공() throws Exception {
        // Given
        Account account = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, account, PROJECT_DEADLINE);

        // When
        Project saveProject = projectService.save(project);

        // Then
        assertThat(saveProject.getName()).isEqualTo(project.getName());
        assertThat(saveProject.getDescription()).isEqualTo(project.getDescription());
        assertThat(saveProject.getCaptain().getId()).isEqualTo(project.getCaptain().getId());
    }

    @Test
    void save_이미존재하는Name으로생성_오류발생() throws Exception {
        // Given
        Account captain = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));
        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);

        projectRepository.save(project);

        // When
        // Then
        String newDescription = "newDescription";
        assertThatThrownBy(() -> projectService.save(new Project(PROJECT_NAME, newDescription, captain, PROJECT_DEADLINE)))
                .isInstanceOf(ProjectNameSameInAccountException.class);
    }

    @Test
    void update_모든정보Update_업데이트성공() throws Exception {
        // Given
        Account captain = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);
        projectRepository.save(project);

        // When
        String newProjectName = "newProjectName";
        String newProjectDescription = "newProjectDescription";
        projectService.update(captain, project.getId(), new ProjectUpdateCondition(newProjectName, newProjectDescription));

        // Then
        assertThat(project.getName()).isEqualTo(newProjectName);
        assertThat(project.getDescription()).isEqualTo(newProjectDescription);
    }

    @Test
    void update_모든정보NULL_업데이트안됨() throws Exception {
        // Given
        Account captain = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);
        projectRepository.save(project);

        // When
        projectService.update(captain, project.getId(), new ProjectUpdateCondition());

        // Then
        assertThat(project.getName()).isEqualTo(PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(PROJECT_DESCRIPTION);
    }

    @Test
    void update_같은정보로업데이트_성공() throws Exception {
        // Given
        Account captain = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);
        projectRepository.save(project);

        // When
        projectService.update(captain, project.getId(), new ProjectUpdateCondition(PROJECT_NAME, PROJECT_DESCRIPTION));

        // Then
        assertThat(project.getName()).isEqualTo(PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(PROJECT_DESCRIPTION);
    }

    @Test
    void update_존재하지않는projectId_오류발생() throws Exception {
        // Given
        Account account = createAccount();

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account, 123L, new ProjectUpdateCondition(PROJECT_NAME, PROJECT_DESCRIPTION)))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void update_captain이아닌Account가요청_오류발생() throws Exception {
        // Given
        Account account1 = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));
        Account account2 = accountService.save(new Account("username2", PASSWORD, "nickname2", PROFILE_IMAGE_FILE, ROLES));

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, account1, PROJECT_DEADLINE);
        projectRepository.save(project);

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account2, project.getId(), new ProjectUpdateCondition("newProjectName", "newProjectDescription")))
                .isInstanceOf(ProjectUpdateAccessDeniedException.class);
    }

    @Test
    void update_같은Account에같은Name존재_오류발생() throws Exception {
        // Given
        Account captain = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);
        projectRepository.save(project);

        Project project2 = new Project("projectName2", "projectDescription2", captain, PROJECT_DEADLINE);
        projectRepository.save(project2);

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(captain, project2.getId(), new ProjectUpdateCondition(PROJECT_NAME, PROJECT_DESCRIPTION)))
                .isInstanceOf(ProjectNameSameInAccountException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    void findAllByCaptain_Account의Project존재_리스트개수(int count) throws Exception {
        // Given
        Account captain = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Project project = new Project(PROJECT_NAME + i, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);
            projectRepository.save(project);
            projects.add(project);
        }

        // When
        int result = projectService.findAllByCaptainNotTerminated(captain).size();

        // Then
        assertThat(result).isEqualTo(count);
    }

    @Test
    void findAllCrew_한명의crew가있을때_한명의crew조회() throws Exception {
        // Given
        Account captain = createAccount();
        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);
        projectRepository.save(project);

        Account crew = new Account("crew", PASSWORD, "crew", PROFILE_IMAGE_FILE, ROLES);
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
        Account newCaptain = accountService.save(new Account(newUsername, newPassword, newNickname, PROFILE_IMAGE_FILE, ROLES));
        Account captain = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);
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
        Account newCaptain = accountService.save(new Account(newUsername, newPassword, newNickname, PROFILE_IMAGE_FILE, ROLES));
        Account captain = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);
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
        Account newCaptain = accountService.save(new Account(newUsername, newPassword, newNickname, PROFILE_IMAGE_FILE, ROLES));
        Account captain = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);
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

        Account newCaptain = accountService.save(new Account(newUsername, newPassword, newNickname, PROFILE_IMAGE_FILE, ROLES));
        Account captain = accountService.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES));

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, PROJECT_DEADLINE);
        projectRepository.save(project);

        accountProjectRepository.save(new AccountProject(newCaptain, project));

        // When
        // Then
        assertThatThrownBy(() -> projectService.updateCaptain(captain, "fail", project.getId()))
                .isInstanceOf(AccountNotFoundException.class);
    }


    private Account createAccount() {
        Account account = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_FILE, ROLES);
        accountRepository.save(account);
        return account;
    }
}