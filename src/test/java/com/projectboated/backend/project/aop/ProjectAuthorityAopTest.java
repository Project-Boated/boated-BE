package com.projectboated.backend.project.aop;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.project.service.ProjectService;
import com.projectboated.backend.project.service.exception.OnlyCaptainException;
import com.projectboated.backend.project.service.exception.OnlyCaptainOrCrewException;
import com.projectboated.backend.security.token.JsonAuthenticationToken;
import com.projectboated.backend.utils.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID2;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("Project Authority : AOP 테스트")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProjectAuthorityAopTest extends BaseTest {

    @MockBean
    ProjectService projectService;

    @Autowired
    ProjectAuthorityAopTestClass target;

    @TestConfiguration
    static class testConfig {

        @Bean
        public ProjectAuthorityAopTestClass ProjectAuthorityAopTestClass() {
            return new ProjectAuthorityAopTestClass();
        }

    }

    @Slf4j
    static class ProjectAuthorityAopTestClass {

        @OnlyCaptain
        public boolean onlyCaptain(Long projectId) {
            return true;
        }

        @OnlyCaptainOrCrew
        public boolean onlyCaptainOrCrew(Long projectId) {
            return true;
        }
    }

    @Test
    void onlyCaptain_captain인경우_넘어감() {
        // Given
        Account captain = Account.builder().id(ACCOUNT_ID).build();
        Project project = Project.builder().captain(captain).build();

        SecurityContextHolder.getContext().setAuthentication(new JsonAuthenticationToken(captain, null));
        when(projectService.findById(PROJECT_ID)).thenReturn(project);

        // When
        boolean result = target.onlyCaptain(PROJECT_ID);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void onlyCaptain_captain아닌경우_예외발생() {
        // Given
        Account captain = Account.builder().id(ACCOUNT_ID).build();
        Account crew = Account.builder().id(ACCOUNT_ID2).build();
        Project project = Project.builder().captain(captain).build();

        SecurityContextHolder.getContext().setAuthentication(new JsonAuthenticationToken(crew, null));
        when(projectService.findById(PROJECT_ID)).thenReturn(project);

        // When
        // Then
        assertThatThrownBy(() -> target.onlyCaptain(PROJECT_ID))
                .isInstanceOf(OnlyCaptainException.class);
    }

    @Test
    void onlyCaptainOrCrew_captain이나crew인경우_넘어감() {
        // Given
        Account captain = Account.builder().id(ACCOUNT_ID).build();
        Project project = Project.builder().captain(captain).build();

        SecurityContextHolder.getContext().setAuthentication(new JsonAuthenticationToken(captain, null));
        when(projectService.findById(PROJECT_ID)).thenReturn(project);
        when(projectService.isCaptainOrCrew(project, captain)).thenReturn(true);

        // When
        boolean result = target.onlyCaptainOrCrew(PROJECT_ID);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void onlyCaptainOrCrew_captain이나crew가아닌경우_예외발생() {
        // Given
        Account captain = Account.builder().id(ACCOUNT_ID).build();
        Account crew = Account.builder().id(ACCOUNT_ID2).build();
        Project project = Project.builder().captain(captain).build();

        SecurityContextHolder.getContext().setAuthentication(new JsonAuthenticationToken(crew, null));
        when(projectService.findById(PROJECT_ID)).thenReturn(project);
        when(projectService.isCaptainOrCrew(project, crew)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> target.onlyCaptainOrCrew(PROJECT_ID))
                .isInstanceOf(OnlyCaptainOrCrewException.class);
    }

}