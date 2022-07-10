package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.NICKNAME2;
import static com.projectboated.backend.common.data.BasicDataAccount.USERNAME2;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountProjectQueryDsl : Persistence 단위 테스트")
class AccountProjectQueryDslRepositoryTest extends RepositoryTest {

    @Test
    void findCrewByProject_crew1명존재_return_해당account() {
        // Given
        Project project = insertDefaultProjectAndDefaultCaptain();
        Account account2 = insertDefaultAccount2();
        insertAccountProject(account2, project);

        // When
        List<Account> result = accountProjectRepository.findCrewByProject(project);

        // Then
        assertThat(result).extracting("username", "nickname")
                .containsExactly(Tuple.tuple(USERNAME2, NICKNAME2));
    }

    @Test
    void findCrewByProject_crew0명존재_return_empty() {
        // Given
        Project project = insertDefaultProjectAndDefaultCaptain();

        // When
        List<Account> result = accountProjectRepository.findCrewByProject(project);

        // Then
        assertThat(result).isEmpty();
    }

}