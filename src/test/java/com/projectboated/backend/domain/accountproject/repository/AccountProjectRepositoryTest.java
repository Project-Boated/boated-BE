package com.projectboated.backend.domain.accountproject.repository;

import com.projectboated.backend.common.basetest.BaseTest;
import com.projectboated.backend.common.data.BasicProjectData;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.projectboated.backend.common.data.BasicAccountData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.context.annotation.ComponentScan.Filter;

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
        Account captain = new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES);
        accountRepository.save(captain);

        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
        projectRepository.save(project);

        int crewsNumber = 3;
        for(int i = 0; i< crewsNumber; i++) {
            Account crew = new Account(ACCOUNT_ID, USERNAME + i, PASSWORD, NICKNAME + i, URL_PROFILE_IMAGE, ROLES);
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
        Account captain = new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES);
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