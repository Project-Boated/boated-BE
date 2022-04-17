package my.sleepydeveloper.projectcompass.domain.accountproject.repository;

import my.sleepydeveloper.projectcompass.common.basetest.BaseTest;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.accountproject.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.PROJECT_DESCRIPTION;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.PROJECT_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.context.annotation.ComponentScan.*;

@DataJpaTest(includeFilters = @Filter(
        type = FilterType.ANNOTATION,
        classes = Repository.class
))
class AccountProjectRepositoryTest extends BaseTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccountProjectRepository accountProjectRepository;

    @Test
    void findCrewsFromProject_crew찾기_정상() throws Exception {
        // Given
        Account captain = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);
        accountRepository.save(captain);

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain);
        projectRepository.save(project);

        int crewsNumber = 3;
        for(int i = 0; i< crewsNumber; i++) {
            Account crew = new Account(USERNAME + i, PASSWORD, NICKNAME + i, PROFILE_IMAGE_URL, ROLES);
            accountRepository.save(crew);
            AccountProject accountProject = new AccountProject(crew, project);
            accountProjectRepository.save(accountProject);
        }

        // When
        List<Account> crews = accountProjectRepository.findCrewsFromProject(project);

        // Then
        assertThat(crews.size()).isEqualTo(crewsNumber);
    }

    @Test
    void delete_accountProject삭제_삭제됨() throws Exception {
        // Given
        Account captain = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);
        accountRepository.save(captain);

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain);
        projectRepository.save(project);

        accountProjectRepository.save(new AccountProject(captain, project));

        // When
        accountProjectRepository.delete(project, captain);

        // Then
        assertThat(accountProjectRepository.findCrewsFromProject(project).size()).isEqualTo(0);
    }
}