package org.projectboated.backend.domain.accountproject.repository;

import org.projectboated.backend.common.basetest.BaseTest;
import org.projectboated.backend.domain.account.repository.AccountRepository;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.accountproject.entity.AccountProject;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.repository.ProjectRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.projectboated.backend.common.data.BasicAccountData;
import org.projectboated.backend.common.data.BasicProjectData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.context.annotation.ComponentScan.*;

@DataJpaTest(includeFilters = @Filter(
        type = FilterType.ANNOTATION,
        classes = Repository.class
))
@Disabled
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
        Account captain = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);
        accountRepository.save(captain);

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project);

        int crewsNumber = 3;
        for(int i = 0; i< crewsNumber; i++) {
            Account crew = new Account(BasicAccountData.USERNAME + i, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME + i, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);
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
        Account captain = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);
        accountRepository.save(captain);

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project);

        accountProjectRepository.save(new AccountProject(captain, project));

        // When
        accountProjectRepository.delete(project, captain);

        // Then
        assertThat(accountProjectRepository.findCrewsFromProject(project).size()).isEqualTo(0);
    }
}