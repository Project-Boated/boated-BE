package org.projectboated.backend.domain.project.service;

import org.projectboated.backend.aws.AwsS3Service;
import org.projectboated.backend.common.basetest.BaseTest;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.exception.AccountNotFoundException;
import org.projectboated.backend.domain.account.repository.AccountRepository;
import org.projectboated.backend.domain.account.service.AccountService;
import org.projectboated.backend.domain.accountproject.entity.AccountProject;
import org.projectboated.backend.domain.accountproject.repository.AccountProjectRepository;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.exception.ProjectNotFoundException;
import org.projectboated.backend.domain.project.exception.ProjectUpdateAccessDeniedException;
import org.projectboated.backend.domain.project.exception.ProjectNameSameInAccountException;
import org.projectboated.backend.domain.project.exception.UpdateCaptainAccessDenied;
import org.projectboated.backend.domain.project.repository.ProjectRepository;
import org.projectboated.backend.domain.project.vo.ProjectUpdateCondition;
import org.projectboated.backend.domain.uploadfile.repository.UploadFileRepository;
import org.projectboated.backend.security.service.KakaoWebService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.projectboated.backend.common.data.BasicAccountData;
import org.projectboated.backend.common.data.BasicProjectData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
        Account account = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, account, BasicProjectData.PROJECT_DEADLINE);

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
        Account captain = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);

        projectRepository.save(project);

        // When
        // Then
        String newDescription = "newDescription";
        assertThatThrownBy(() -> projectService.save(new Project(BasicProjectData.PROJECT_NAME, newDescription, captain, BasicProjectData.PROJECT_DEADLINE)))
                .isInstanceOf(ProjectNameSameInAccountException.class);
    }

    @Test
    void update_모든정보Update_업데이트성공() throws Exception {
        // Given
        Account captain = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project);

        // When
        String newProjectName = "newProjectName";
        String newProjectDescription = "newProjectDescription";
        LocalDateTime newDeadline = BasicProjectData.PROJECT_DEADLINE.plus(1, ChronoUnit.DAYS);
        projectService.update(captain, project.getId(), new ProjectUpdateCondition(newProjectName, newProjectDescription, newDeadline));

        // Then
        assertThat(project.getName()).isEqualTo(newProjectName);
        assertThat(project.getDescription()).isEqualTo(newProjectDescription);
        assertThat(project.getDeadline()).isEqualTo(newDeadline);
    }

    @Test
    void update_모든정보NULL_업데이트안됨() throws Exception {
        // Given
        Account captain = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project);

        // When
        projectService.update(captain, project.getId(), new ProjectUpdateCondition());

        // Then
        assertThat(project.getName()).isEqualTo(BasicProjectData.PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(BasicProjectData.PROJECT_DESCRIPTION);
    }

    @Test
    void update_같은정보로업데이트_성공() throws Exception {
        // Given
        Account captain = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project);

        // When
        projectService.update(captain, project.getId(), new ProjectUpdateCondition(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE));

        // Then
        assertThat(project.getName()).isEqualTo(BasicProjectData.PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(BasicProjectData.PROJECT_DESCRIPTION);
        assertThat(project.getDeadline()).isEqualTo(BasicProjectData.PROJECT_DEADLINE);
    }

    @Test
    void update_존재하지않는projectId_오류발생() throws Exception {
        // Given
        Account account = createAccount();

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account, 123L, new ProjectUpdateCondition(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE)))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void update_captain이아닌Account가요청_오류발생() throws Exception {
        // Given
        Account account1 = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        Account account2 = accountService.save(new Account("username2", BasicAccountData.PASSWORD, "nickname2", BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, account1, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project);

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(account2, project.getId(), new ProjectUpdateCondition("newProjectName", "newProjectDescription", null)))
                .isInstanceOf(ProjectUpdateAccessDeniedException.class);
    }

    @Test
    void update_같은Account에같은Name존재_오류발생() throws Exception {
        // Given
        Account captain = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project);

        Project project2 = new Project("projectName2", "projectDescription2", captain, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project2);

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(captain, project2.getId(), new ProjectUpdateCondition(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE)))
                .isInstanceOf(ProjectNameSameInAccountException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    void findAllByCaptain_Account의Project존재_리스트개수(int count) throws Exception {
        // Given
        Account captain = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Project project = new Project(BasicProjectData.PROJECT_NAME + i, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
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
        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project);

        Account crew = new Account("crew", BasicAccountData.PASSWORD, "crew", BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);
        accountRepository.save(crew);

        AccountProject accountProject = new AccountProject(crew, project);
        accountProjectRepository.save(accountProject);

        // When
        List<Account> crews = projectService.findAllCrews(captain, project.getId());

        // Then
        assertThat(crews.size()).isEqualTo(1);
    }

    @Test
    void findAllCrew_존재하지않는ProjectId_오류발생() throws Exception {
        // Given
        Account captain = createAccount();
        // When
        // Then
        assertThatThrownBy(() -> projectService.findAllCrews(captain, 1L))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void updateCaptain_새로운captain으로업데이트_업데이트성공() throws Exception {
        // Given
        String newUsername = "newUsername";
        String newNickname = "newNickname";
        String newPassword = "newPassword";
        Account newCaptain = accountService.save(new Account(newUsername, newPassword, newNickname, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        Account captain = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
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
        Account newCaptain = accountService.save(new Account(newUsername, newPassword, newNickname, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        Account captain = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
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
        Account newCaptain = accountService.save(new Account(newUsername, newPassword, newNickname, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        Account captain = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
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

        Account newCaptain = accountService.save(new Account(newUsername, newPassword, newNickname, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        Account captain = accountService.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project);

        accountProjectRepository.save(new AccountProject(newCaptain, project));

        // When
        // Then
        assertThatThrownBy(() -> projectService.updateCaptain(captain, "fail", project.getId()))
                .isInstanceOf(AccountNotFoundException.class);
    }


    private Account createAccount() {
        Account account = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);
        accountRepository.save(account);
        return account;
    }
}