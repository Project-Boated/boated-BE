package org.projectboated.backend.domain.project.entity;

import org.projectboated.backend.common.basetest.BaseTest;
import org.projectboated.backend.domain.account.entity.Account;
import org.junit.jupiter.api.Test;
import org.projectboated.backend.common.data.BasicAccountData;
import org.projectboated.backend.common.data.BasicProjectData;

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectTest extends BaseTest {

    private String newProjectName = "newName";
    private String newProjectDescription = "newDescription";

    private String newCaptainUsername = "newUsername";
    private String newCaptainNickname = "newNickname";

    @Test
    void constructor_Project생성_성공() {
        // Given
        Account captain = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);

        // When
        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);

        // Then
        assertThat(project.getName()).isEqualTo(BasicProjectData.PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(BasicProjectData.PROJECT_DESCRIPTION);
        assertThat(project.getCaptain()).isEqualTo(captain);
    }

    @Test
    void changeProjectInform_모든정보업데이트_업데이트성공() throws Exception {
        // Given
        Account captain = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);
        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);
        
        // When
        project.changeProjectInform(newProjectName, newProjectDescription, BasicProjectData.PROJECT_DEADLINE.plus(1, ChronoUnit.DAYS));
        
        // Then
        assertThat(project.getName()).isEqualTo(newProjectName);
        assertThat(project.getDescription()).isEqualTo(newProjectDescription);
    }

    @Test
    void changeProjectInform_모든요청NULL_기존정보유지() throws Exception {
        // Given
        Account captain = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);
        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, captain, BasicProjectData.PROJECT_DEADLINE);

        // When
        project.changeProjectInform(null, null, null);

        // Then
        assertThat(project.getName()).isEqualTo(BasicProjectData.PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(BasicProjectData.PROJECT_DESCRIPTION);
        assertThat(project.getDeadline()).isEqualTo(BasicProjectData.PROJECT_DEADLINE);
    }

    @Test
    void changeCaptain_captain바꾸기_바꾸기성공() throws Exception {
        // Given
        Account existingCaptain = new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);
        Project project = new Project(BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, existingCaptain, BasicProjectData.PROJECT_DEADLINE);

        // When
        Account newCaptain = new Account(newCaptainUsername, BasicAccountData.PASSWORD, newCaptainNickname, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES);
        project.changeCaptain(newCaptain);

        // Then
        assertThat(project.getCaptain().getUsername()).isEqualTo(newCaptainUsername);
        assertThat(project.getCaptain().getNickname()).isEqualTo(newCaptainNickname);
    }

}