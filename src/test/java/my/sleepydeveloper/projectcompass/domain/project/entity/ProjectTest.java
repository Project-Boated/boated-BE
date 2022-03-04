package my.sleepydeveloper.projectcompass.domain.project.entity;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static my.sleepydeveloper.projectcompass.common.data.AccountBasicData.*;
import static my.sleepydeveloper.projectcompass.common.data.ProjectBasicData.projectDescription;
import static my.sleepydeveloper.projectcompass.common.data.ProjectBasicData.projectName;
import static org.assertj.core.api.Assertions.*;

class ProjectTest {
    
    @Test
    @DisplayName("project 정보 업데이트, 모든 요청포함")
    void changeProjectInform_모든요청포함() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, "USER_ROLE");
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
    @DisplayName("project 정보 업데이트, 모든 요청 NULL")
    void changeProjectInform_모든요청NULL() throws Exception {
        // Given
        Account account = new Account(username, password, nickname, "USER_ROLE");
        Project project = new Project(projectName, projectDescription, account);

        // When
        project.changeProjectInform(null, null);

        // Then
        assertThat(project.getName()).isEqualTo(projectName);
        assertThat(project.getDescription()).isEqualTo(projectDescription);
    }

    @Test
    @DisplayName("change_Captain_정상")
    void changeCaptain_정상() throws Exception {
        // Given
        Account captain = new Account(username, password, nickname, "USER_ROLE");
        Project project = new Project(projectName, projectDescription, captain);

        // When
        String newNickname = "new";
        String newUsername = "new";
        String newPassword = "new";
        project.changeCaptain(new Account(newUsername, newPassword, newNickname, "ROLE_USER"));

        // Then
        assertThat(project.getCaptain().getUsername()).isEqualTo(newUsername);
        assertThat(project.getCaptain().getNickname()).isEqualTo(newNickname);
        assertThat(project.getCaptain().getPassword()).isEqualTo(newPassword);
    }

}