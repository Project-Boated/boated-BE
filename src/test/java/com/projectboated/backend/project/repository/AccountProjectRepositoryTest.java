package com.projectboated.backend.project.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.entity.AccountProject;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.utils.base.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountProject : Persistence 단위 테스트")
class AccountProjectRepositoryTest extends RepositoryTest {

    @Test
    void deleteByProjectAndAccount_존재하는Project와Account_delete정상() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        AccountProject accountProject = insertAccountProject(account, project);

        // When
        accountProjectRepository.deleteByProjectAndAccount(project, account);
        flushAndClear();

        // Then
        assertThat(accountProjectRepository.findById(accountProject.getId())).isEmpty();
    }

    @Test
    void countByCrewInProject_crew1명존재_return_1() {
        // Given
        Account account = insertAccount(USERNAME, NICKNAME);
        Project project = insertProject(account);

        Account account2 = insertAccount(USERNAME2, NICKNAME2);
        accountProjectRepository.save(AccountProject.builder()
                .project(project)
                .account(account2)
                .build());

        // When
        Long result = accountProjectRepository.countByCrewInProject(account2, project);

        // Then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void countByCrewInProject_crew0명존재_return_0() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);

        // When
        Long result = accountProjectRepository.countByCrewInProject(account, project);

        // Then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void findByAccountAndProject_1개존재_1개조회() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        AccountProject accountProject = insertAccountProject(account, project);

        // When
        Optional<AccountProject> result = accountProjectRepository.findByAccountAndProject(account, project);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(accountProject);
    }

    @Test
    void findByAccount_2개존재_2개조회() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Project project2 = insertProject(account);
        AccountProject accountProject = insertAccountProject(account, project);
        AccountProject accountProject2 = insertAccountProject(account, project2);

        // When
        List<AccountProject> result = accountProjectRepository.findByAccount(account);

        // Then
        assertThat(result).containsExactly(accountProject, accountProject2);
    }

}