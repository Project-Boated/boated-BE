package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.common.data.BasicProjectData;
import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.common.basetest.BaseTest;
import com.projectboated.backend.domain.account.repository.AccountRepository;
import com.projectboated.backend.domain.project.entity.Project;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Disabled
class ProjectRepositoryTest extends BaseTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void existsByNameAndCaptain_projectName이Captain에있음_true() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        Project project = projectRepository.save(new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE));

        // When
        boolean result = projectRepository.existsByNameAndCaptain(project.getName(), captain);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByNameAndCaptain_projectName이Captain에없음_false() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        Project project = projectRepository.save(new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE));
        Account newAccount = accountRepository.save(new Account("newUsername", BasicAccountData.PASSWORD, "newNickname", BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        boolean result = projectRepository.existsByNameAndCaptain(project.getName(), newAccount);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByName_존재하는name으로조회_true() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        Project project = projectRepository.save(new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE));

        // When
        boolean result = projectRepository.existsByName(BasicProjectData.PROJECT_NAME);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByName_존재하지않는name으로조회_false() throws Exception {
        // Given
        // When
        boolean result = projectRepository.existsByName(BasicProjectData.PROJECT_NAME);

        // Then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    void findAllByCaptain_account에project저장됨_account가소유한project개수(int count) throws Exception {
        // Given
        Account account = accountRepository.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            projectRepository.save(new Project(BasicProjectData.PROJECT_NAME + "i", BasicProjectData.PROJECT_DESCRIPTION, account, BasicProjectData.PROJECT_DEADLINE));
        }

        // When
        List<Project> findByAll = projectRepository.findAllByCaptain(account);

        // Then
        assertThat(findByAll.size()).isEqualTo(count);
    }
}