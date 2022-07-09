package com.projectboated.backend.domain.project.entity;

import com.projectboated.backend.common.basetest.BaseTest;
import com.projectboated.backend.common.data.BasicDataAccount;
import com.projectboated.backend.common.data.BasicDataProject;
import com.projectboated.backend.domain.account.account.entity.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectTest extends BaseTest {

    private String newProjectName = "newName";
    private String newProjectDescription = "newDescription";

    private String newCaptainUsername = "newUsername";
    private String newCaptainNickname = "newNickname";

    @Test
    void constructor_Project생성_성공() {
        // Given
        Account captain = new Account(ACCOUNT_ID, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD, BasicDataAccount.NICKNAME, BasicDataAccount.URL_PROFILE_IMAGE, BasicDataAccount.ROLES);

        // When
        Project project = new Project(captain, BasicDataProject.PROJECT_NAME, BasicDataProject.PROJECT_DESCRIPTION, BasicDataProject.PROJECT_DEADLINE);

        // Then
        assertThat(project.getName()).isEqualTo(BasicDataProject.PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(BasicDataProject.PROJECT_DESCRIPTION);
        Assertions.assertThat(project.getCaptain()).isEqualTo(captain);
    }

    @Test
    void changeProjectInform_모든정보업데이트_업데이트성공() throws Exception {
        // Given
        Account captain = new Account(ACCOUNT_ID, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD, BasicDataAccount.NICKNAME, BasicDataAccount.URL_PROFILE_IMAGE, BasicDataAccount.ROLES);
        Project project = new Project(captain, BasicDataProject.PROJECT_NAME, BasicDataProject.PROJECT_DESCRIPTION, BasicDataProject.PROJECT_DEADLINE);
        
        // When
        project.changeProjectInform(newProjectName, newProjectDescription, BasicDataProject.PROJECT_DEADLINE.plus(1, ChronoUnit.DAYS));
        
        // Then
        assertThat(project.getName()).isEqualTo(newProjectName);
        assertThat(project.getDescription()).isEqualTo(newProjectDescription);
    }

    @Test
    void changeProjectInform_모든요청NULL_기존정보유지() throws Exception {
        // Given
        Account captain = new Account(ACCOUNT_ID, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD, BasicDataAccount.NICKNAME, BasicDataAccount.URL_PROFILE_IMAGE, BasicDataAccount.ROLES);
        Project project = new Project(captain, BasicDataProject.PROJECT_NAME, BasicDataProject.PROJECT_DESCRIPTION, BasicDataProject.PROJECT_DEADLINE);

        // When
        project.changeProjectInform(null, null, null);

        // Then
        assertThat(project.getName()).isEqualTo(BasicDataProject.PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(BasicDataProject.PROJECT_DESCRIPTION);
        assertThat(project.getDeadline()).isEqualTo(BasicDataProject.PROJECT_DEADLINE);
    }

    @Test
    void changeCaptain_captain바꾸기_바꾸기성공() throws Exception {
        // Given
        Account existingCaptain = new Account(ACCOUNT_ID, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD, BasicDataAccount.NICKNAME, BasicDataAccount.URL_PROFILE_IMAGE, BasicDataAccount.ROLES);
        Project project = new Project(existingCaptain, BasicDataProject.PROJECT_NAME, BasicDataProject.PROJECT_DESCRIPTION, BasicDataProject.PROJECT_DEADLINE);

        // When
        Account newCaptain = new Account(ACCOUNT_ID, newCaptainUsername, BasicDataAccount.PASSWORD, newCaptainNickname, BasicDataAccount.URL_PROFILE_IMAGE, BasicDataAccount.ROLES);
        project.changeCaptain(newCaptain);

        // Then
        Assertions.assertThat(project.getCaptain().getUsername()).isEqualTo(newCaptainUsername);
        Assertions.assertThat(project.getCaptain().getNickname()).isEqualTo(newCaptainNickname);
    }

}