package my.sleepydeveloper.projectcompass.common.utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.web.invitation.document.InvitationDocument.documentInvitationAccept;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvitationTestUtils {
    public static int createInvitation(int port, Cookie captainCookie, Long projectId, String crewNickname) {
        return given()
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/crews?nickname={nickname}", projectId, crewNickname)
        .thenReturn().body().jsonPath().getInt("id");
    }

    public static Response getMyInvitations(int port, Cookie crewCookie) {
        return given()
                .accept(ContentType.JSON)
                .cookie(crewCookie)
            .when()
                .port(port)
                .get("/api/account/invitations")
            .then().extract().response();
    }

    public static void acceptInvitation(int port, Cookie crewCookie, Long invitationId) {
        given()
            .accept(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
            .post("/api/account/invitations/{invitationId}/accept", invitationId)
        .then().extract().body();
    }
}
