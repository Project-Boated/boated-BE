package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.common.data.BasicDataAccount;
import com.projectboated.backend.common.data.BasicDataProject;
import com.projectboated.backend.common.utils.InvitationTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.web.project.dto.request.UpdateProjectCaptainRequest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static com.projectboated.backend.web.project.controller.document.ProjectCaptainDocument.documentProjectCaptainUpdate;

@AutoConfigureMockMvc
class ProjectCaptainControllerTest extends AcceptanceTest {

    @Test
    void updateCaptain_capatin업데이트_정상() throws Exception {
        // Given
        String crewUsername = "crewUsername";
        String crewNickname = "crewNickname";

        AccountTestUtils.createAccount(port, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD, BasicDataAccount.NICKNAME, BasicDataAccount.PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, BasicDataAccount.PASSWORD, crewNickname, BasicDataAccount.PROFILE_IMAGE_URL);
        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, BasicDataAccount.PASSWORD);
        Cookie captainCookie = AccountTestUtils.login(port, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, BasicDataProject.PROJECT_NAME, BasicDataProject.PROJECT_DESCRIPTION, BasicDataProject.PROJECT_DEADLINE);
        int invitationId = InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);
        InvitationTestUtils.acceptInvitation(port, crewCookie, (long)invitationId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectCaptainUpdate())
            .contentType(ContentType.JSON)
            .body(new UpdateProjectCaptainRequest(crewNickname))
            .cookie(captainCookie)
        .when()
            .port(port)
            .put("/api/projects/{projectId}/captain", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}