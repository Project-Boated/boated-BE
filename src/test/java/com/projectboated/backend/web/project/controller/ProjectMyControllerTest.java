package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.common.data.BasicProjectData;
import com.projectboated.backend.common.utils.InvitationTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static com.projectboated.backend.web.project.controller.document.ProjectMyDocument.documentProjectMyRetrieve;

@AutoConfigureMockMvc
class ProjectMyControllerTest extends AcceptanceTest {

    @Test
    void getMyProjects_내가가진프로젝트조회_정상() throws Exception {
        // Given
        String crewUsername = "crewUsername";
        String crewNickname = "crewNickname";

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, BasicAccountData.PASSWORD, crewNickname, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, BasicAccountData.PASSWORD);
        Cookie captainCookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);
        int invitationId = InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);
        InvitationTestUtils.acceptInvitation(port, crewCookie, (long)invitationId);

        ProjectTestUtils.createProject(port, crewCookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);

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