package com.projectboated.backend.common.utils;

import com.projectboated.backend.security.dto.UsernamePasswordDto;
import com.projectboated.backend.web.account.account.dto.request.SignUpRequest;
import io.restassured.http.Cookie;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public abstract class AccountTestUtils {

    public static void createAccount(int port, String username, String password, String nickname) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new SignUpRequest(username, password, nickname))
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
