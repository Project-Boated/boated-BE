package com.projectboated.backend.project.project.entity;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.entity.exception.ProjectTotalFileSizeIsMinusException;
import com.projectboated.backend.project.project.service.condition.ProjectUpdateCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.projectboated.backend.utils.data.BasicDataAccount.*;
import static com.projectboated.backend.utils.data.BasicDataProject.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Project : Entity 단위 테스트")
class ProjectTest {

    private String newProjectName = "newName";
    private String newProjectDescription = "newDescription";
    private String newCaptainUsername = "newUsername";
    private String newCaptainNickname = "newNickname";

    @Test
    void 생성자_Project생성_return_생성된Project() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
        ;

        // When
        Project project = new Project(PROJECT_ID, captain, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        // Then
        assertThat(project.getCaptain()).isEqualTo(captain);
        assertThat(project.getName()).isEqualTo(PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(PROJECT_DESCRIPTION);
        assertThat(project.getDeadline()).isEqualTo(PROJECT_DEADLINE);
        assertThat(project.isTerminated()).isEqualTo(false);
    }


    @Test
    void changeName_null이아닌name_이름변경() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
        ;

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        // When
        project.changeName(newProjectName);

        // Then
        assertThat(project.getName()).isEqualTo(newProjectName);
    }

    @Test
    void changeName_null인name_이름변경안함() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
        ;

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        // When
        project.changeName(null);

        // Then
        assertThat(project.getName()).isEqualTo(PROJECT_NAME);
    }

    @Test
    void changeDescription_null이아닌description_설명변경() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
        ;

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        // When
        project.changeDescription(newProjectDescription);

        // Then
        assertThat(project.getDescription()).isEqualTo(newProjectDescription);
    }

    @Test
    void changeDescription_null인description_설명변경안함() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
        ;

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        // When
        project.changeDescription(null);

        // Then
        assertThat(project.getDescription()).isEqualTo(PROJECT_DESCRIPTION);
    }

    @Test
    void changeDeadline_다른deadline입력_deadline변경() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
        ;

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        // When
        LocalDateTime newDeadline = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        project.changeDeadline(newDeadline);

        // Then
        assertThat(project.getDeadline()).isEqualTo(newDeadline);
    }

    @Test
    void changeDeadline_null인deadline_deadline변경안함() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
        ;

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        // When
        project.changeDeadline(null);

        // Then
        assertThat(project.getDeadline()).isEqualTo(PROJECT_DEADLINE);
    }

    @Test
    void changeCaptain_다른Captain입력_Captain변경() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
        ;

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        Account newCaptain = ACCOUNT2;

        // When
        project.changeCaptain(newCaptain);

        // Then
        assertThat(project.getCaptain()).isEqualTo(newCaptain);
    }

    @Test
    void terminate_terminate되지않은project_terminate됨() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
        ;

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        // When
        project.terminate();

        // Then
        assertThat(project.isTerminated()).isTrue();
    }

    @Test
    void cancelTerminate_terminate된project_terminate취소() {
        // Given
        Account captain = Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
        ;

        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        project.terminate();

        // When
        project.cancelTerminate();

        // Then
        assertThat(project.isTerminated()).isFalse();
    }

    @Test
    void isCaptain_captain인경우_return_true() {
        // Given
        Account captain = Account.builder()
                .id(1L)
                .build();
        Project project = Project.builder()
                .captain(captain)
                .build();

        // When
        boolean result = project.isCaptain(captain);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void isCaptain_captain이아닌경우_return_false() {
        // Given
        Account captain = Account.builder()
                .id(1L)
                .build();
        Project project = Project.builder()
                .captain(captain)
                .build();

        Account notCaptain = Account.builder()
                .id(2L)
                .build();

        // When
        boolean result = project.isCaptain(notCaptain);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void update_전체바꾸기_업데이트성공() {
        // Given
        Account captain = Account.builder()
                .build();
        Project project = Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        // When
        LocalDateTime newDeadline = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        ProjectUpdateCond cond = ProjectUpdateCond.builder()
                .name(newProjectName)
                .deadline(newDeadline)
                .description(newProjectDescription)
                .build();
        project.update(cond);

        // Then
        assertThat(project.getName()).isEqualTo(newProjectName);
        assertThat(project.getDeadline()).isEqualTo(newDeadline);
        assertThat(project.getDescription()).isEqualTo(newProjectDescription);
    }

    @Test
    void minusTotalFileSize_마이너스가되는경우_예외발생() {
        // Given
        Project project = Project.builder()
                .build();

        // When
        // Then
        assertThatThrownBy(() -> project.minusTotalFileSize(10L))
                .isInstanceOf(ProjectTotalFileSizeIsMinusException.class);
    }

    @Test
    void minusTotalFileSize_0이되는경우_0으로바뀜() {
        // Given
        Project project = Project.builder()
                .build();
        project.addTotalFileSize(10L);

        // When
        project.minusTotalFileSize(10L);

        // Then
        assertThat(project.getTotalFileSize()).isEqualTo(0);
    }

}