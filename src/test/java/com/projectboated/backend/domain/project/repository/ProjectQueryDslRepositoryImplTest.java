package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.common.data.BasicDataAccount;
import com.projectboated.backend.common.data.BasicDataProject;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.service.condition.GetMyProjectsCond;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(AccountProjectRepository.class)
class ProjectQueryDslRepositoryImplTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccountProjectRepository accountProjectRepository;


    /*@Test
    public void getMyProject_모든경우프로젝트1개있음_프로젝트4개조회() {
        // Given
        Account account1 = new Account(USERNAME, PASSWORD, NICKNAME, null, null);
        accountRepository.save(account1);
        Account account2 = new Account("crewUsername", PASSWORD, "crewNickname", null, null);
        accountRepository.save(account2);

        Project project1 = new Project(PROJECT_NAME+1, PROJECT_DESCRIPTION, account1, LocalDateTime.now());
        projectRepository.save(project1);
        Project project2 = new Project(PROJECT_NAME+2, PROJECT_DESCRIPTION, account1, LocalDateTime.now());
        project2.terminateProject();
        projectRepository.save(project2);

        Project project3 = new Project(PROJECT_NAME+3, PROJECT_DESCRIPTION, account2, LocalDateTime.now());
        projectRepository.save(project3);
        Project project4 = new Project(PROJECT_NAME+4, PROJECT_DESCRIPTION, account2, LocalDateTime.now());
        project4.terminateProject();
        projectRepository.save(project4);


        AccountProject accountProject1 = new AccountProject(account2, project1);
        accountProjectRepository.save(accountProject1);
        AccountProject accountProject2 = new AccountProject(account2, project2);
        accountProjectRepository.save(accountProject2);

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .crewTerm(true)
                .crewNotTerm(true)
                .captainTerm(true)
                .captainNotTerm(true)
                .pageable(PageRequest.of(0, 20))
                .build();

        // When
        List<Project> list = projectRepository.getMyProjects(account2, cond);

        // Then
        assertThat(list).hasSize(4);
    }

    @Test
    public void getMyProject_모든경우프로젝트1개있음_crew인종료되지않은프로젝트1개조회_1개조회() {
        // Given
        Account account1 = new Account(USERNAME, PASSWORD, NICKNAME, null, null);
        accountRepository.save(account1);
        Account account2 = new Account("crewUsername", PASSWORD, "crewNickname", null, null);
        accountRepository.save(account2);

        Project project1 = new Project(PROJECT_NAME+1, PROJECT_DESCRIPTION, account1, LocalDateTime.now());
        projectRepository.save(project1);
        Project project2 = new Project(PROJECT_NAME+2, PROJECT_DESCRIPTION, account1, LocalDateTime.now());
        project2.terminateProject();
        projectRepository.save(project2);

        Project project3 = new Project(PROJECT_NAME+3, PROJECT_DESCRIPTION, account2, LocalDateTime.now());
        projectRepository.save(project3);
        Project project4 = new Project(PROJECT_NAME+4, PROJECT_DESCRIPTION, account2, LocalDateTime.now());
        project4.terminateProject();
        projectRepository.save(project4);


        AccountProject accountProject1 = new AccountProject(account2, project1);
        accountProjectRepository.save(accountProject1);
        AccountProject accountProject2 = new AccountProject(account2, project2);
        accountProjectRepository.save(accountProject2);

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .crewTerm(false)
                .crewNotTerm(true)
                .captainTerm(false)
                .captainNotTerm(false)
                .pageable(PageRequest.of(0, 20))
                .build();

        // When
        List<Project> list = projectRepository.getMyProjects(account2, cond);

        // Then
        assertThat(list).extracting("name").containsExactly("name1");
    }

    @Test
    public void getMyProject_모든경우프로젝트1개있음_crew인종료된프로젝트1개조회_1개조회() {
        // Given
        Account account1 = new Account(USERNAME, PASSWORD, NICKNAME, null, null);
        accountRepository.save(account1);
        Account account2 = new Account("crewUsername", PASSWORD, "crewNickname", null, null);
        accountRepository.save(account2);

        Project project1 = new Project(PROJECT_NAME+1, PROJECT_DESCRIPTION, account1, LocalDateTime.now());
        projectRepository.save(project1);
        Project project2 = new Project(PROJECT_NAME+2, PROJECT_DESCRIPTION, account1, LocalDateTime.now());
        project2.terminateProject();
        projectRepository.save(project2);

        Project project3 = new Project(PROJECT_NAME+3, PROJECT_DESCRIPTION, account2, LocalDateTime.now());
        projectRepository.save(project3);
        Project project4 = new Project(PROJECT_NAME+4, PROJECT_DESCRIPTION, account2, LocalDateTime.now());
        project4.terminateProject();
        projectRepository.save(project4);


        AccountProject accountProject1 = new AccountProject(account2, project1);
        accountProjectRepository.save(accountProject1);
        AccountProject accountProject2 = new AccountProject(account2, project2);
        accountProjectRepository.save(accountProject2);

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .crewTerm(true)
                .crewNotTerm(false)
                .captainTerm(false)
                .captainNotTerm(false)
                .pageable(PageRequest.of(0, 20))
                .build();

        // When
        List<Project> list = projectRepository.getMyProjects(account2, cond);

        // Then
        assertThat(list).extracting("name").containsExactly("name2");
    }

    @Test
    public void getMyProject_모든경우프로젝트1개있음_captain인종료되지않은프로젝트1개조회_1개조회() {
        // Given
        Account account1 = new Account(USERNAME, PASSWORD, NICKNAME, null, null);
        accountRepository.save(account1);
        Account account2 = new Account("crewUsername", PASSWORD, "crewNickname", null, null);
        accountRepository.save(account2);

        Project project1 = new Project(PROJECT_NAME+1, PROJECT_DESCRIPTION, account1, LocalDateTime.now());
        projectRepository.save(project1);
        Project project2 = new Project(PROJECT_NAME+2, PROJECT_DESCRIPTION, account1, LocalDateTime.now());
        project2.terminateProject();
        projectRepository.save(project2);

        Project project3 = new Project(PROJECT_NAME+3, PROJECT_DESCRIPTION, account2, LocalDateTime.now());
        projectRepository.save(project3);
        Project project4 = new Project(PROJECT_NAME+4, PROJECT_DESCRIPTION, account2, LocalDateTime.now());
        project4.terminateProject();
        projectRepository.save(project4);


        AccountProject accountProject1 = new AccountProject(account2, project1);
        accountProjectRepository.save(accountProject1);
        AccountProject accountProject2 = new AccountProject(account2, project2);
        accountProjectRepository.save(accountProject2);

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .crewTerm(false)
                .crewNotTerm(false)
                .captainTerm(false)
                .captainNotTerm(true)
                .pageable(PageRequest.of(0, 20))
                .build();

        // When
        List<Project> list = projectRepository.getMyProjects(account2, cond);

        // Then
        assertThat(list).extracting("name").containsExactly("name3");
    }

    @Test
    public void getMyProject_모든경우프로젝트1개있음_captain인종료된프로젝트1개조회_1개조회() {
        // Given
        Account account1 = new Account(USERNAME, PASSWORD, NICKNAME, null, null);
        accountRepository.save(account1);
        Account account2 = new Account("crewUsername", PASSWORD, "crewNickname", null, null);
        accountRepository.save(account2);

        Project project1 = new Project(PROJECT_NAME+1, PROJECT_DESCRIPTION, account1, LocalDateTime.now());
        projectRepository.save(project1);
        Project project2 = new Project(PROJECT_NAME+2, PROJECT_DESCRIPTION, account1, LocalDateTime.now());
        project2.terminateProject();
        projectRepository.save(project2);

        Project project3 = new Project(PROJECT_NAME+3, PROJECT_DESCRIPTION, account2, LocalDateTime.now());
        projectRepository.save(project3);
        Project project4 = new Project(PROJECT_NAME+4, PROJECT_DESCRIPTION, account2, LocalDateTime.now());
        project4.terminateProject();
        projectRepository.save(project4);


        AccountProject accountProject1 = new AccountProject(account2, project1);
        accountProjectRepository.save(accountProject1);
        AccountProject accountProject2 = new AccountProject(account2, project2);
        accountProjectRepository.save(accountProject2);

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .crewTerm(false)
                .crewNotTerm(false)
                .captainTerm(true)
                .captainNotTerm(false)
                .pageable(PageRequest.of(0, 20))
                .build();

        // When
        List<Project> list = projectRepository.getMyProjects(account2, cond);

        // Then
        assertThat(list).extracting("name").containsExactly("name4");
    }

    @Test
    public void getMyProject_captain인종료되지않은프로젝트1개있음_프로젝트1개조회() {
        // Given
        Account captain = new Account(USERNAME, PASSWORD, NICKNAME, null, null);
        accountRepository.save(captain);

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, LocalDateTime.now());
        projectRepository.save(project);

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .crewTerm(false)
                .crewNotTerm(false)
                .captainTerm(false)
                .captainNotTerm(true)
                .pageable(PageRequest.of(0, 20))
                .build();
        
        // When
        List<Project> list = projectRepository.getMyProjects(captain, cond);

        // Then
        assertThat(list).extracting("name", "description").contains(tuple(PROJECT_NAME, PROJECT_DESCRIPTION));
    }

    @Test
    public void getMyProject_captain인종료된프로젝트1개있음_로젝트1개조회() {
        // Given
        Account captain = new Account(USERNAME, PASSWORD, NICKNAME, null, null);
        accountRepository.save(captain);

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, LocalDateTime.now());
        project.terminateProject();
        projectRepository.save(project);

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .crewTerm(false)
                .crewNotTerm(false)
                .captainTerm(true)
                .captainNotTerm(false)
                .pageable(PageRequest.of(0, 20))
                .build();

        // When
        List<Project> list = projectRepository.getMyProjects(captain, cond);

        // Then
        assertThat(list).extracting("name", "description").contains(tuple(PROJECT_NAME, PROJECT_DESCRIPTION));
    }

    @Test
    public void getMyProject_crew인종료되지않은프로젝트1개있음_프로젝트1개조회() {
        // Given
        Account captain = new Account(USERNAME, PASSWORD, NICKNAME, null, null);
        accountRepository.save(captain);
        Account crew = new Account("crewUsername", PASSWORD, "crewNickname", null, null);
        accountRepository.save(crew);

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, LocalDateTime.now());
        projectRepository.save(project);

        AccountProject accountProject = new AccountProject(crew, project);
        accountProjectRepository.save(accountProject);

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .crewTerm(false)
                .crewNotTerm(true)
                .captainTerm(false)
                .captainNotTerm(false)
                .pageable(PageRequest.of(0, 20))
                .build();

        // When
        List<Project> list = projectRepository.getMyProjects(crew, cond);

        // Then
        assertThat(list).extracting("name", "description").contains(tuple(PROJECT_NAME, PROJECT_DESCRIPTION));
    }

    @Test
    public void getMyProject_crew인종료된프로젝트1개있음_프로젝트1개조회() {
        // Given
        Account captain = new Account(USERNAME, PASSWORD, NICKNAME, null, null);
        accountRepository.save(captain);
        Account crew = new Account("crewUsername", PASSWORD, "crewNickname", null, null);
        accountRepository.save(crew);

        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain, LocalDateTime.now());
        project.terminateProject();
        projectRepository.save(project);

        AccountProject accountProject = new AccountProject(crew, project);
        accountProjectRepository.save(accountProject);

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .crewTerm(true)
                .crewNotTerm(false)
                .captainTerm(false)
                .captainNotTerm(false)
                .pageable(PageRequest.of(0, 20))
                .build();

        // When
        List<Project> list = projectRepository.getMyProjects(crew, cond);

        // Then
        assertThat(list).extracting("name", "description").contains(tuple(PROJECT_NAME, PROJECT_DESCRIPTION));
    }*/
}