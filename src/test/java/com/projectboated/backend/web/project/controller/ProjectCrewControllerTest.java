package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.common.data.BasicProjectData;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.common.utils.InvitationTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static com.projectboated.backend.web.project.controller.document.ProjectCrewDocument.documentProjectsCrewDelete;
import static com.projectboated.backend.web.project.controller.document.ProjectCrewDocument.documentProjectsCrewsRetrieve;
import static io.restassured.RestAssured.given;

@AutoConfigureMockMvc
class ProjectCrewControllerTest extends AcceptanceTest {

    final String CREW_USERNAME = "crewUsername";
    final String CREW_NICKNAME = "crewNickname";

    @Autowired
    MockMvc mockMvc;

    @Test
    void getCrews_project에속한모든crew조회_성공() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, CREW_USERNAME, BasicAccountData.PASSWORD, CREW_NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);
        Cookie crewCookie = AccountTestUtils.login(port, CREW_USERNAME, BasicAccountData.PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);
        InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, CREW_NICKNAME);

        long invitationId = InvitationTestUtils.getMyInvitations(port, crewCookie).getBody().jsonPath().getLong("invitations[0].id");
        InvitationTestUtils.acceptInvitation(port, crewCookie, invitationId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectsCrewsRetrieve())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
            .get("/api/projects/{projectId}/crews", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteCrew_project에속한모든crew추방_성공() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, CREW_USERNAME, BasicAccountData.PASSWORD, CREW_NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);
        Cookie crewCookie = AccountTestUtils.login(port, CREW_USERNAME, BasicAccountData.PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);
        InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, CREW_NICKNAME);

        long invitationId = InvitationTestUtils.getMyInvitations(port, crewCookie).getBody().jsonPath().getLong("invitations[0].id");
        InvitationTestUtils.acceptInvitation(port, crewCookie, invitationId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectsCrewDelete())
            .cookie(captainCookie)
        .when()
            .port(port)
            .delete("/api/projects/{projectId}/crews/{crewNickname}", projectId, CREW_NICKNAME)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}