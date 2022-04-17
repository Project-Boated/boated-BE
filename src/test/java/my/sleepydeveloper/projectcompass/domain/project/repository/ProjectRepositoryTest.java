package my.sleepydeveloper.projectcompass.domain.project.repository;

import my.sleepydeveloper.projectcompass.common.basetest.BaseTest;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.PROJECT_DESCRIPTION;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.PROJECT_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProjectRepositoryTest extends BaseTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void existsByNameAndCaptain_projectName이Captain에있음_true() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));
        Project project = projectRepository.save(new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain));

        // When
        boolean result = projectRepository.existsByNameAndCaptain(project.getName(), captain);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByNameAndCaptain_projectName이Captain에없음_false() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));
        Project project = projectRepository.save(new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain));
        Account newAccount = accountRepository.save(new Account("newUsername", PASSWORD, "newNickname", PROFILE_IMAGE_URL, ROLES));

        // When
        boolean result = projectRepository.existsByNameAndCaptain(project.getName(), newAccount);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByName_존재하는name으로조회_true() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));
        Project project = projectRepository.save(new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain));

        // When
        boolean result = projectRepository.existsByName(PROJECT_NAME);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByName_존재하지않는name으로조회_false() throws Exception {
        // Given
        // When
        boolean result = projectRepository.existsByName(PROJECT_NAME);

        // Then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    void findAllByCaptain_account에project저장됨_account가소유한project개수(int count) throws Exception {
        // Given
        Account account = accountRepository.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            projectRepository.save(new Project(PROJECT_NAME + "i", PROJECT_DESCRIPTION, account));
        }

        // When
        List<Project> findByAll = projectRepository.findAllByCaptain(account);

        // Then
        assertThat(findByAll.size()).isEqualTo(count);
    }
}