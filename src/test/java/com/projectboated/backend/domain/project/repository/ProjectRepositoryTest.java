package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Project : Persistence 단위 테스트")
class ProjectRepositoryTest extends RepositoryTest {
    
    @Test
    void existsByName_존재하는name일경우_return_true() {
        // Given
        insertDefaultProjectAndDefaultCaptain();
        
        // When
        boolean result = projectRepository.existsByName(PROJECT_NAME);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByName_존재하지않는name일경우_return_false() {
        // Given
        // When
        boolean result = projectRepository.existsByName(PROJECT_NAME);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByNameAndCaptain_존재하는name과captain일경우_return_true() {
        // Given
        Account captain = insertDefaultAccount();
        insertDefaultProject(captain);

        // When
        boolean result = projectRepository.existsByNameAndCaptain(PROJECT_NAME, captain);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByNameAndCaptain_존재하는name과존재하지않는captain일경우_return_false() {
        // Given
        Account captain = insertDefaultAccount();
        insertDefaultProject(captain);

        Account unknown = insertDefaultAccount2();

        // When
        boolean result = projectRepository.existsByNameAndCaptain(PROJECT_NAME, unknown);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByNameAndCaptain_존재하지않는name과존재하는captain일경우_return_false() {
        // Given
        Account captain = insertDefaultAccount();
        insertDefaultProject(captain);

        // When
        boolean result = projectRepository.existsByNameAndCaptain("fail", captain);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByNameAndCaptain_존재하지않는name과존재하지않는captain일경우_return_false() {
        // Given
        Account captain = insertDefaultAccount();
        insertDefaultProject(captain);

        Account unknown = insertDefaultAccount2();

        // When
        boolean result = projectRepository.existsByNameAndCaptain("fail", unknown);

        // Then
        assertThat(result).isFalse();
    }



}