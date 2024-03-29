package com.projectboated.backend.project.project.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.project.service.ProjectTerminateService;
import com.projectboated.backend.project.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.utils.base.ServiceTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ProjectTerminateServiceTest extends ServiceTest {

    @InjectMocks
    ProjectTerminateService projectTerminateService;

    @Mock
    ProjectRepository projectRepository;

    @Test
    void terminateProject_projectId찾을수없음_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectTerminateService.terminateProject(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void terminateProject_정상request_terminate정상() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        // When
        projectTerminateService.terminateProject(project.getId());

        // Then
        assertThat(project.isTerminated()).isTrue();
    }

    @Test
    void cancelTerminateProject_projectId찾을수없음_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectTerminateService.cancelTerminateProject(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void cancelTerminateProject_정상request_terminate정상() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        // When
        projectTerminateService.cancelTerminateProject(PROJECT_ID);

        // Then
        assertThat(project.isTerminated()).isFalse();
    }

}