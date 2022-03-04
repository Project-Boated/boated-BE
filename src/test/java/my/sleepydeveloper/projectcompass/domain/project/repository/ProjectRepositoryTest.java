package my.sleepydeveloper.projectcompass.domain.project.repository;

import my.sleepydeveloper.projectcompass.common.config.TestConfigRegisterAccountService;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.parameters.P;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static my.sleepydeveloper.projectcompass.common.data.AccountBasicData.nickname;
import static my.sleepydeveloper.projectcompass.common.data.AccountBasicData.username;
import static my.sleepydeveloper.projectcompass.common.data.AccountBasicData.password;

import static my.sleepydeveloper.projectcompass.common.data.ProjectBasicData.projectName;
import static my.sleepydeveloper.projectcompass.common.data.ProjectBasicData.projectDescription;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(TestConfigRegisterAccountService.class)
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountTestUtils accountTestUtils;

    @Autowired
    EntityManager em;


    @Test
    @DisplayName("save_작동확인")
    void save_작동확인() throws Exception {
        // Given
        Account account = accountTestUtils.signUpUser(username, password, nickname);

        // When
        Project project = projectRepository.save(new Project(projectName, projectDescription, account));

        // Then
        assertThat(project.getName()).isEqualTo(projectName);
        assertThat(project.getDescription()).isEqualTo(projectDescription);
        assertThat(project.getCaptain()).isEqualTo(account);
    }

    @Test
    @DisplayName("existsByNameAndCaptain_작동확인_true일경우")
    void existsByNameAndCaptain_작동확인_true일경우() throws Exception {
        // Given
        Account account = accountTestUtils.signUpUser(username, password, nickname);
        Project project = projectRepository.save(new Project(projectName, projectDescription, account));

        // When
        boolean result = projectRepository.existsByNameAndCaptain(project.getName(), account);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("existsByNameAndCaptain_작동확인_false일경우_다른account")
    void existsByNameAndCaptain_작동확인_다른account() throws Exception {
        // Given
        Account account = accountTestUtils.signUpUser(username, password, nickname);
        Account newAccount = accountTestUtils.signUpUser("newUsername", password, "newNickname");
        Project project = projectRepository.save(new Project(projectName, projectDescription, account));

        // When
        boolean result = projectRepository.existsByNameAndCaptain(project.getName(), newAccount);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsByNameAndCaptain_작동확인_false일경우_다른projectName")
    void existsByNameAndCaptain_작동확인_다른projectName() throws Exception {
        // Given
        Account account = accountTestUtils.signUpUser(username, password, nickname);
        Project project = projectRepository.save(new Project(projectName, projectDescription, account));

        // When
        boolean result = projectRepository.existsByNameAndCaptain("newProjectName", account);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("findAllByAccount_정상_동작확인")
    void findAllByAccount_정상_동작확인() throws Exception {
        // Given
        Account account = accountTestUtils.signUpUser(username, password, nickname);
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            projectRepository.save(new Project(projectName + "i", projectDescription, account));
        }

        // When
        List<Project> findByAll = projectRepository.findAllByCaptain(account);

        // Then
        assertThat(findByAll.size()).isEqualTo(10);
    }
}