package my.sleepydeveloper.projectcompass.domain.project.repository;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.projectDescription;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.projectName;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void existsByNameAndCaptain_projectName이Captain에있음_true() throws Exception {
        // Given
        Account account = accountRepository.save(new Account(username, password, nickname, userRole));
        Project project = projectRepository.save(new Project(projectName, projectDescription, account));

        // When
        boolean result = projectRepository.existsByNameAndCaptain(project.getName(), account);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByNameAndCaptain_projectName이Captain에없음_false() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(username, password, nickname, userRole));
        Project project = projectRepository.save(new Project(projectName, projectDescription, captain));
        Account newAccount = accountRepository.save(new Account("newUsername", password, "newNickname", userRole));

        // When
        boolean result = projectRepository.existsByNameAndCaptain(project.getName(), newAccount);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByNameAndCaptainAndNotProject_projectName이Captain에있는데Project와동일_false() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(username, password, nickname, userRole));
        Project project = projectRepository.save(new Project(projectName, projectDescription, captain));

        // When
        Long result = projectRepository.countByNameAndCaptainAndNotProject(project.getName(), captain, project);

        // Then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void existsByNameAndCaptainAndNotProject_projectName이Captain에있는데다른Project_false() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(username, password, nickname, userRole));
        Project project = projectRepository.save(new Project(projectName, projectDescription, captain));
        Project project2 = projectRepository.save(new Project("projectName2", "projectDescription2", captain));

        // When
        Long result = projectRepository.countByNameAndCaptainAndNotProject(project.getName(), captain, project2);

        // Then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void findAllByAccount_account에project저장됨_account가소유한project개수() throws Exception {
        // Given
        Account account = accountRepository.save(new Account(username, password, nickname, userRole));
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