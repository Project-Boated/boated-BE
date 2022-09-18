package com.projectboated.backend.project.project.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.utils.base.RepositoryTest;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.projectboated.backend.utils.data.BasicDataAccount.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountProjectQueryDsl : Persistence 단위 테스트")
class AccountProjectQueryDslRepositoryTest extends RepositoryTest {

    @Test
    void findCrewByProject_crew1명존재_return_해당account() {
        // Given
        Project project = insertProjectAndCaptain(USERNAME, NICKNAME);
        Account account2 = insertAccount(USERNAME2, NICKNAME2);
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
        Project project = insertProjectAndCaptain();

        // When
        List<Account> result = accountProjectRepository.findCrewByProject(project);

        // Then
        assertThat(result).isEmpty();
    }

}