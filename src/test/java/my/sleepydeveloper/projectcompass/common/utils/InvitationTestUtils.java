package my.sleepydeveloper.projectcompass.common.utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import my.sleepydeveloper.projectcompass.web.project.dto.CreateProjectRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.web.invitation.document.InvitationDocument.documentInvitationCreate;
import static org.hamcrest.Matchers.notNullValue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvitationTestUtils {
    public static void createInvitation(int port, Cookie captainCookie, Long projectId, String crewNickname) {
        given()
                .accept(ContentType.JSON)
                .cookie(captainCookie)
        .when()
                .port(port)
                .post("/api/projects/{projectId}/crews?nickname={nickname}", projectId, crewNickname)
        .then().statusCode(HttpStatus.OK.value());
    }
}
