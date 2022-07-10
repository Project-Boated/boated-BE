package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.AccountProject;
import com.projectboated.backend.domain.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountProject : Persistence 단위 테스트")
class AccountProjectRepositoryTest extends RepositoryTest {

    @Test
    void deleteByProjectAndAccount_존재하는Project와Account_delete정상() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);

        AccountProject accountProject = new AccountProject(account, project);
        accountProjectRepository.save(accountProject);

        // When
        accountProjectRepository.deleteByProjectAndAccount(project, account);
        flushAndClear();

        // Then
        assertThat(accountProjectRepository.findById(accountProject.getId())).isEmpty();
    }
    
    @Test
    void countByProjectAndAccount_1개존재하는Project와Account_return_1() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);

        Account account2 = insertDefaultAccount2();
        accountProjectRepository.save(AccountProject.builder()
                .project(project)
                .account(account2)
                .build());

        // When
        long result = accountProjectRepository.countByProjectIdAndAccountId(project.getId(), account2.getId());

        // Then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void countByProjectAndAccount_0개존재하는Project와Account_return_0() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);

        Account account2 = insertDefaultAccount2();

        // When
        long result = accountProjectRepository.countByProjectIdAndAccountId(project.getId(), account2.getId());

        // Then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void countByCrewInProject_crew1명존재_return_1() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);

        Account account2 = insertDefaultAccount2();
        accountProjectRepository.save(AccountProject.builder()
                .project(project)
                .account(account2)
                .build());

        // When
        Long result = accountProjectRepository.countByCrewInProject(account2.getId(), project.getId());

        // Then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void countByCrewInProject_crew0명존재_return_0() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);

        // When
        Long result = accountProjectRepository.countByCrewInProject(123L, project.getId());

        // Then
        assertThat(result).isEqualTo(0);
    }

}