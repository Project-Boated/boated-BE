package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Project : Persistence 단위 테스트")
class ProjectRepositoryTest extends RepositoryTest {

    @Test
    void existsByNameAndCaptain_존재하는name과captain일경우_return_true() {
        // Given
        Account captain = insertAccount();
        insertProject(captain);

        // When
        boolean result = projectRepository.existsByNameAndCaptain(PROJECT_NAME, captain);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByNameAndCaptain_존재하는name과존재하지않는captain일경우_return_false() {
        // Given
        Account captain = insertAccount(USERNAME, NICKNAME);
        insertProject( captain);

        Account unknown = insertAccount(USERNAME2, NICKNAME2);

        // When
        boolean result = projectRepository.existsByNameAndCaptain(PROJECT_NAME, unknown);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByNameAndCaptain_존재하지않는name과존재하는captain일경우_return_false() {
        // Given
        Account captain = insertAccount();
        insertProject(captain);

        // When
        boolean result = projectRepository.existsByNameAndCaptain("fail", captain);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByNameAndCaptain_존재하지않는name과존재하지않는captain일경우_return_false() {
        // Given
        Account captain = insertAccount(USERNAME, NICKNAME);
        insertProject(captain);

        Account unknown = insertAccount(USERNAME2, NICKNAME2);

        // When
        boolean result = projectRepository.existsByNameAndCaptain("fail", unknown);

        // Then
        assertThat(result).isFalse();
    }



}