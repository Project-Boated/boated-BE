package org.projectboated.backend.web.invitation;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.projectboated.backend.common.basetest.AcceptanceTest;
import org.projectboated.backend.common.utils.AccountTestUtils;
import org.projectboated.backend.common.utils.InvitationTestUtils;
import org.projectboated.backend.common.utils.ProjectTestUtils;
import org.junit.jupiter.api.Test;
import org.projectboated.backend.common.data.BasicAccountData;
import org.projectboated.backend.common.data.BasicProjectData;
import org.projectboated.backend.web.invitation.document.InvitationDocument;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class InvitationControllerTest extends AcceptanceTest {

    private static String crewUsername = "crew";
    private static String crewNickname = "crew";

    @Test
    void inviteCrew_account초대하기_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, BasicAccountData.PASSWORD, crewNickname, BasicAccountData.PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, captainCookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);

        // When
        // Then
        given(this.spec)
            .filter(InvitationDocument.documentInvitationCreate())
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/crews?nickname={nickname}", projectId, crewNickname)
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("id", notNullValue());
    }

    @Test
    void getMyInvitation_내Invitation확인_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, BasicAccountData.PASSWORD, crewNickname, BasicAccountData.PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, captainCookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);

        InvitationTestUtils.createInvitation(port, captainCookie, (long)projectId, crewNickname);

        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, BasicAccountData.PASSWORD);

        // When
        // Then
        given(this.spec)
            .filter(InvitationDocument.documentMyInvitationRetrieve())
            .accept(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
            .get("/api/account/invitations")
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("invitations.size()", is(1));
    }

    @Test
    void acceptInvitation_프로젝트초대accept_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, BasicAccountData.PASSWORD, crewNickname, BasicAccountData.PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, captainCookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);

        InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);

        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, BasicAccountData.PASSWORD);
        long invitationId = InvitationTestUtils.getMyInvitations(port, crewCookie).getBody().jsonPath().getLong("invitations[0].id");

        // When
        // Then
        given(this.spec)
            .filter(InvitationDocument.documentInvitationAccept())
            .accept(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
            .post("/api/account/invitations/{invitationId}/accept", invitationId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("id", notNullValue());
    }

    @Test
    void rejectInvitation_프로젝트초대reject_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, BasicAccountData.PASSWORD, crewNickname, BasicAccountData.PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, captainCookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);

        InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);

        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, BasicAccountData.PASSWORD);
        long invitationId = InvitationTestUtils.getMyInvitations(port, crewCookie).getBody().jsonPath().getLong("invitations[0].id");

        // When
        // Then
        given(this.spec)
            .filter(InvitationDocument.documentInvitationReject())
            .accept(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
            .post("/api/account/invitations/{invitationId}/reject", invitationId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("id", notNullValue());
    }

}