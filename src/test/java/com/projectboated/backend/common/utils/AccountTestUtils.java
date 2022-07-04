package com.projectboated.backend.common.utils;

import com.projectboated.backend.security.dto.UsernamePasswordDto;
import com.projectboated.backend.web.account.account.dto.request.SignUpRequest;
import io.restassured.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
