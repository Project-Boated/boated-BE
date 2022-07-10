package com.projectboated.backend.web.invitation;

import com.projectboated.backend.common.data.BasicDataAccount;
import com.projectboated.backend.common.data.BasicDataProject;
import com.projectboated.backend.common.utils.InvitationTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import com.projectboated.backend.web.invitation.document.InvitationDocument;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class InvitationControllerTest extends AcceptanceTest {

    private static String crewUsername = "crewNickname";
    private static String crewNickname = "crewUsername";

    @Test
    void inviteCrew_account초대하기_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

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
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        InvitationTestUtils.createInvitation(port, captainCookie, (long)projectId, crewNickname);

        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, PASSWORD);

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
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);

        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, PASSWORD);
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
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);

        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, PASSWORD);
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