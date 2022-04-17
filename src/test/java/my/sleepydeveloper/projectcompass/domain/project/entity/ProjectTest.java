package my.sleepydeveloper.projectcompass.domain.project.entity;

import my.sleepydeveloper.projectcompass.common.basetest.BaseTest;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import org.junit.jupiter.api.Test;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.PROJECT_DESCRIPTION;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.PROJECT_NAME;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectTest extends BaseTest {

    private String newProjectName = "newName";
    private String newProjectDescription = "newDescription";

    private String newCaptainUsername = "newUsername";
    private String newCaptainNickname = "newNickname";

    @Test
    void constructor_Project생성_성공() {
        // Given
        Account captain = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);

        // When
        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain);

        // Then
        assertThat(project.getName()).isEqualTo(PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(PROJECT_DESCRIPTION);
        assertThat(project.getCaptain()).isEqualTo(captain);
    }

    @Test
    void changeProjectInform_모든정보업데이트_업데이트성공() throws Exception {
        // Given
        Account captain = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);
        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain);
        
        // When
        project.changeProjectInform(newProjectName, newProjectDescription);
        
        // Then
        assertThat(project.getName()).isEqualTo(newProjectName);
        assertThat(project.getDescription()).isEqualTo(newProjectDescription);
    }

    @Test
    void changeProjectInform_모든요청NULL_기존정보유지() throws Exception {
        // Given
        Account captain = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);
        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain);

        // When
        project.changeProjectInform(null, null);

        // Then
        assertThat(project.getName()).isEqualTo(PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(PROJECT_DESCRIPTION);
    }

    @Test
    void changeCaptain_captain바꾸기_바꾸기성공() throws Exception {
        // Given
        Account existingCaptain = new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES);
        Project project = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, existingCaptain);

        // When
        Account newCaptain = new Account(newCaptainUsername, PASSWORD, newCaptainNickname, PROFILE_IMAGE_URL, ROLES);
        project.changeCaptain(newCaptain);

        // Then
        assertThat(project.getCaptain().getUsername()).isEqualTo(newCaptainUsername);
        assertThat(project.getCaptain().getNickname()).isEqualTo(newCaptainNickname);
    }

}