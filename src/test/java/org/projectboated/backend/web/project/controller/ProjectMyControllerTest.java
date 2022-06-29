package org.projectboated.backend.web.project.controller;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.projectboated.backend.common.basetest.AcceptanceTest;
import org.projectboated.backend.common.utils.AccountTestUtils;
import org.projectboated.backend.common.utils.InvitationTestUtils;
import org.projectboated.backend.common.utils.ProjectTestUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.projectboated.backend.common.data.BasicAccountData.*;
import static org.projectboated.backend.common.data.BasicProjectData.*;
import static org.projectboated.backend.web.project.controller.document.ProjectDocument.documentProjectCrew;
import static org.projectboated.backend.web.project.controller.document.ProjectMyDocument.documentProjectMyRetrieve;

@AutoConfigureMockMvc
class ProjectMyControllerTest extends AcceptanceTest {

    @Test
    void getMyProjects_내가가진프로젝트조회_정상() throws Exception {
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

        ProjectTestUtils.createProject(port, crewCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        // When
        // Then
        given(this.spec)
            .param("captain", "term,not-term")
            .param("crew", "term,not-term")
            .param("page", "0")
            .param("size", "1")
            .param("sort", "name,desc")
            .param("sort", "deadline,asc")
            .param("sort", "createdDate,asc")
            .filter(documentProjectMyRetrieve())
            .accept(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
        .get("/api/projects/my")
            .then()
            .statusCode(HttpStatus.OK.value());
    }

}