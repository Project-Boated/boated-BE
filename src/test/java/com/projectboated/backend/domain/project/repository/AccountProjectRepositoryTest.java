package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.ACCOUNT_PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
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

}