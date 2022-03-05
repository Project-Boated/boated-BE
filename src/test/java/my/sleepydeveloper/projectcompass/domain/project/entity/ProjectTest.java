package my.sleepydeveloper.projectcompass.domain.project.entity;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import org.junit.jupiter.api.Test;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.projectDescription;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.projectName;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectTest {
    
    @Test
    void changeProjectInform_모든정보업데이트_업데이트성공() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, userRole);
        Project project = new Project(projectName, projectDescription, account);
        
        // When
        String newName = "newName";
        String newDescription = "newDescription";
        project.changeProjectInform(newName, newDescription);
        
        // Then
        assertThat(project.getName()).isEqualTo(newName);
        assertThat(project.getDescription()).isEqualTo(newDescription);
    }

    @Test
    void changeProjectInform_모든요청NULL_기존정보유지() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, userRole);
        Project project = new Project(projectName, projectDescription, account);

        // When
        project.changeProjectInform(null, null);

        // Then
        assertThat(project.getName()).isEqualTo(projectName);
        assertThat(project.getDescription()).isEqualTo(projectDescription);
    }

    @Test
    void changeCaptain_captain바꾸기_바꾸기성공() throws Exception {
        // Given
        Account existingCaptain = new Account(username, password, nickname, "USER_ROLE");
        Project project = new Project(projectName, projectDescription, existingCaptain);

        // When
        String newCaptainUsername = "newUsername";
        String newCaptainPassword = "newPassword";
        String newCaptainNickname = "newNickname";
        project.changeCaptain(new Account(newCaptainUsername, newCaptainPassword, newCaptainNickname, userRole));

        // Then
        assertThat(project.getCaptain().getUsername()).isEqualTo(newCaptainUsername);
        assertThat(project.getCaptain().getNickname()).isEqualTo(newCaptainNickname);
        assertThat(project.getCaptain().getPassword()).isEqualTo(newCaptainPassword);
    }

}