package my.sleepydeveloper.projectcompass.common.utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.ResponseBody;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.security.dto.UsernamePasswordDto;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountUpdateRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.SignUpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.PASSWORD;
import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.PROFILE_IMAGE_URL;
import static my.sleepydeveloper.projectcompass.web.account.controller.document.AccountDocument.documentAccountProfileUpdate;

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
