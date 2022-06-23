package org.projectboated.backend.web.project.controller;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.projectboated.backend.common.basetest.AcceptanceTest;
import org.projectboated.backend.common.utils.AccountTestUtils;
import org.projectboated.backend.common.utils.InvitationTestUtils;
import org.projectboated.backend.common.utils.ProjectTestUtils;
import org.projectboated.backend.domain.project.service.ProjectService;
import org.projectboated.backend.web.project.dto.CreateProjectRequest;
import org.projectboated.backend.web.project.dto.PatchProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.projectboated.backend.common.data.BasicAccountData.*;
import static org.projectboated.backend.common.data.BasicProjectData.*;
import static org.projectboated.backend.web.project.controller.document.ProjectDocument.*;

@AutoConfigureMockMvc
class ProjectControllerTest extends AcceptanceTest {

    @Autowired
    ProjectTestUtils projectTestUtils;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProjectService projectService;

    @Test
    void createProject_프로젝트생성_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        // When
        // Then
        given(this.spec)
                .filter(documentProjectCreate())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new CreateProjectRequest(PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE))
        .when()
                .port(port)
                .post("/api/projects")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("id", notNullValue());
    }


    @Test
    void patchProject_모든필드업데이트_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        String projectUpdateName = "updatedProjectName";
        String projectUpdateDescription = "updateDescription";

        // When
        // Then
        given(this.spec)
                .filter(documentProjectUpdate())
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new PatchProjectRequest(projectUpdateName, projectUpdateDescription, LocalDateTime.now().plus(1, ChronoUnit.DAYS)))
        .when()
                .port(port)
                .patch("/api/projects/" + projectId)
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("id", notNullValue());
    }

    @Test
    void deleteProject_프로젝트삭제_정상() {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        given(this.spec)
            .filter(documentProjectDelete())
            .contentType(ContentType.JSON)
            .cookie(cookie)
        .when()
            .port(port)
            .delete("/api/projects/{projectId}", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getMyCaptainProject_내가Captain인프로젝트조회_정상() throws Exception {
        // Given
        String crewUsername = "crewUsername";
        String crewNickname = "crewNickname";

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);
        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, PASSWORD);
        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int invitationId = InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);
        InvitationTestUtils.acceptInvitation(port, crewCookie, (long)invitationId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectCaptain())
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .get("/api/projects/my/captain")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getMyCrewProject_내가Crew인프로젝트조회_정상() throws Exception {
        // Given
        String crewUsername = "crewUsername";
        String crewNickname = "crewNickname";

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);
        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, PASSWORD);
        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int invitationId = InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);
        InvitationTestUtils.acceptInvitation(port, crewCookie, (long)invitationId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectCrew())
            .accept(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
            .get("/api/projects/my/crew")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getMyCaptainTerminatedProject_내가Captain인종료된프로젝트조회_정상() throws Exception {
        // Given
        String crewUsername = "crewUsername";
        String crewNickname = "crewNickname";

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);
        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, PASSWORD);
        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int invitationId = InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);
        InvitationTestUtils.acceptInvitation(port, crewCookie, (long)invitationId);

        ProjectTestUtils.terminateProject(port, captainCookie, (long)projectId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectCaptainTerminated())
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .get("/api/projects/my/captain/terminated")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getMyCrewTerminatedProject_내가Crew인종료된프로젝트조회_정상() throws Exception {
        // Given
        String crewUsername = "crewUsername";
        String crewNickname = "crewNickname";

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);
        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, PASSWORD);
        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int invitationId = InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);
        InvitationTestUtils.acceptInvitation(port, crewCookie, (long)invitationId);

        ProjectTestUtils.terminateProject(port, captainCookie, (long)projectId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectCrewTerminated())
            .accept(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
            .get("/api/projects/my/crew/terminated")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void terminateProject_프로젝트를종료시킴_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectTerminate())
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/terminate", projectId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("id", equalTo(projectId));
    }

    @Test
    void cancelTerminateProject_종료된프로젝트를시작_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        ProjectTestUtils.terminateProject(port, captainCookie, projectId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectCancelTerminate())
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/cancel-terminate", projectId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("id", equalTo(projectId));
    }

    // Invitation 구현이 끝나면 다시 테스팅, 현재는 테스트 불가
//    @Test
//    void updateCaptain_capatin업데이트_정상() throws Exception {
//        // Given
//        Account captain = accountTestUtils.getAccountFromSecurityContext();
//        Project project = projectTestUtils.createProject(captain, projectName, projectDescription);
//        String newCaptainUsername = "newCaptain";
//        String newCaptainPassword = "newCaptain";
//        String newCaptainNickname = "newCaptain";
//        Account newCaptain = accountTestUtils.signUp(newCaptainUsername, newCaptainPassword, newCaptainNickname, "ROLE_USER");
//        AccountProject accountProject = accountProjectTestUtils.createAccountRepository(newCaptain, project);
//
//        // When
//        // Then
//        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/projects/{projectId}/captain", project.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new UpdateCaptainRequest(newCaptainUsername))))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("id").value(newCaptain.getId()))
//                .andDo(documentProjectUpdateCaptain());
//    }

    @Test
    void getProject_프로젝트프로필조회_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectRetrieve())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
        .when()
            .port(port)
            .get("/api/projects/{projectId}", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }
}