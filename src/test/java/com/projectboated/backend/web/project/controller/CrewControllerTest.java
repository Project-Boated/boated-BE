package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.common.data.BasicProjectData;
import com.projectboated.backend.common.utils.InvitationTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import com.projectboated.backend.common.utils.AccountTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.given;
import static com.projectboated.backend.web.project.controller.document.ProjectDocument.documentProjectRetrieveCrews;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

@AutoConfigureMockMvc
class CrewControllerTest extends AcceptanceTest {

    final String CREW_USERNAME = "crewUsername";
    final String CREW_NICKNAME = "crewUsername";

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
            .filter(documentProjectRetrieveCrews())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
            .get("/api/projects/{projectId}/crews", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}