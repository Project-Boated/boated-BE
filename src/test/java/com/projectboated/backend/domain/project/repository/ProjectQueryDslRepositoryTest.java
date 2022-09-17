package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.project.service.condition.GetMyProjectsCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("AccountProjectQueryDsl: Persistence 단위 테스트")
class ProjectQueryDslRepositoryTest extends RepositoryTest {

    @Test
    public void getMyProject_모든경우프로젝트1개있음_return_프로젝트4개() {
        // Given
        Account account1 = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
        accountRepository.save(account1);
        Account account2 = Account.builder()
                .username("crewUsername")
                .password(PASSWORD)
                .nickname("crewNickname")
                .build();
        accountRepository.save(account2);

        Project project1 = Project.builder()
                .captain(account1)
                .name(PROJECT_NAME + 1)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project1);
        Project project2 = Project.builder()
                .captain(account1)
                .name(PROJECT_NAME + 2)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project2.terminate();
        projectRepository.save(project2);

        Project project3 = Project.builder()
                .captain(account2)
                .name(PROJECT_NAME + 3)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project3);
        Project project4 = Project.builder()
                .captain(account2)
                .name(PROJECT_NAME + 4)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project4.terminate();
        projectRepository.save(project4);

        insertAccountProject(account2, project1);
        insertAccountProject(account2, project2);

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
    public void getMyProject_모든경우프로젝트1개있음_crew인종료되지않은프로젝트1개조회_return_1개() {
        // Given
        Account account1 = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
        accountRepository.save(account1);
        Account account2 = Account.builder()
                .username("crewUsername")
                .password(PASSWORD)
                .nickname("crewNickname")
                .build();
        accountRepository.save(account2);

        Project project1 = Project.builder()
                .captain(account1)
                .name(PROJECT_NAME + 1)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project1);
        Project project2 = Project.builder()
                .captain(account1)
                .name(PROJECT_NAME + 2)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project2.terminate();
        projectRepository.save(project2);

        Project project3 = Project.builder()
                .captain(account2)
                .name(PROJECT_NAME + 3)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project3);
        Project project4 = Project.builder()
                .captain(account2)
                .name(PROJECT_NAME + 4)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project4.terminate();
        projectRepository.save(project4);


        insertAccountProject(account2, project1);
        insertAccountProject(account2, project2);

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
    public void getMyProject_모든경우프로젝트1개있음_crew인종료된프로젝트1개조회_return_1개() {
        // Given
        Account account1 = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
        accountRepository.save(account1);
        Account account2 = Account.builder()
                .username("crewUsername")
                .password(PASSWORD)
                .nickname("crewNickname")
                .build();
        accountRepository.save(account2);

        Project project1 = Project.builder()
                .captain(account1)
                .name(PROJECT_NAME + 1)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project1);
        Project project2 = Project.builder()
                .captain(account1)
                .name(PROJECT_NAME + 2)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project2.terminate();
        projectRepository.save(project2);

        Project project3 = Project.builder()
                .captain(account2)
                .name(PROJECT_NAME + 3)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project3);
        Project project4 = Project.builder()
                .captain(account2)
                .name(PROJECT_NAME + 4)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project4.terminate();
        projectRepository.save(project4);

        insertAccountProject(account2, project1);
        insertAccountProject(account2, project2);

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
    public void getMyProject_모든경우프로젝트1개있음_captain인종료되지않은프로젝트1개조회_return_1개() {
        // Given
        Account account1 = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
        accountRepository.save(account1);
        Account account2 = Account.builder()
                .username("crewUsername")
                .password(PASSWORD)
                .nickname("crewNickname")
                .build();
        accountRepository.save(account2);

        Project project1 = Project.builder()
                .captain(account1)
                .name(PROJECT_NAME + 1)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project1);
        Project project2 = Project.builder()
                .captain(account1)
                .name(PROJECT_NAME + 2)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project2.terminate();
        projectRepository.save(project2);

        Project project3 = Project.builder()
                .captain(account2)
                .name(PROJECT_NAME + 3)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project3);
        Project project4 = Project.builder()
                .captain(account2)
                .name(PROJECT_NAME + 4)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project4.terminate();
        projectRepository.save(project4);

        insertAccountProject(account2, project1);
        insertAccountProject(account2, project2);

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
    public void getMyProject_모든경우프로젝트1개있음_captain인종료된프로젝트1개조회_return_1개조회() {
        // Given
        Account account1 = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
        accountRepository.save(account1);
        Account account2 = Account.builder()
                .username("crewUsername")
                .password(PASSWORD)
                .nickname("crewNickname")
                .build();
        accountRepository.save(account2);

        Project project1 = Project.builder()
                .captain(account1)
                .name(PROJECT_NAME + 1)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project1);
        Project project2 = Project.builder()
                .captain(account1)
                .name(PROJECT_NAME + 2)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project2.terminate();
        projectRepository.save(project2);

        Project project3 = Project.builder()
                .captain(account2)
                .name(PROJECT_NAME + 3)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project3);
        Project project4 = Project.builder()
                .captain(account2)
                .name(PROJECT_NAME + 4)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project4.terminate();
        projectRepository.save(project4);

        insertAccountProject(account2, project1);
        insertAccountProject(account2, project2);

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
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
        accountRepository.save(captain);

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
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
    public void getMyProject_captain인종료된프로젝트1개있음_return_1개조회() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
        accountRepository.save(captain);

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project);
        project.terminate();
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
    public void getMyProject_crew인종료되지않은프로젝트1개있음_return_프로젝트1개() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
        accountRepository.save(captain);
        Account crew = Account.builder()
                .username("crewUsername")
                .password(PASSWORD)
                .nickname("crewNickname")
                .build();
        accountRepository.save(crew);

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        projectRepository.save(project);

        insertAccountProject(crew, project);

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
    public void getMyProject_crew인종료된프로젝트1개있음_return_프로젝트1개() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
        accountRepository.save(captain);
        Account crew = Account.builder()
                .username("crewUsername")
                .password(PASSWORD)
                .nickname("crewNickname")
                .build();
        accountRepository.save(crew);

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(LocalDateTime.now())
                .build();
        project.terminate();
        projectRepository.save(project);

        insertAccountProject(crew, project);

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
    }
}