package my.sleepydeveloper.projectcompass.common.utils;

import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.security.dto.UsernamePasswordDto;
import my.sleepydeveloper.projectcompass.web.account.dto.SignUpRequest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class AccountTestUtils {

    public static void createAccount(int port, String username, String password, String nickname, String profileImageUrl) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new SignUpRequest(username, password, nickname, profileImageUrl))
        .when()
            .port(port)
            .post("/api/account/sign-up");
    }

    public static Cookie login(int port, String username, String password) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new UsernamePasswordDto(username, password))
        .when()
            .port(port)
            .post("/api/sign-in")
        .thenReturn()
                .getDetailedCookie("JSESSIONID");
    }

}
