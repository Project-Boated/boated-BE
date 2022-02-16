package com.example.projectcompass.web.account.controller;

import com.example.projectcompass.security.dto.UsernamePasswordDto;
import com.example.projectcompass.web.account.utils.AccountTestUtils;
import com.example.projectcompass.web.common.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.example.projectcompass.web.account.documentfilter.AccountDocument.documentSignIn;
import static com.example.projectcompass.web.account.documentfilter.AccountDocument.documentSignUp;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;


class AccountControllerTest extends AcceptanceTest {

    @Autowired
    AccountTestUtils accountTestUtils;

    public String username = "user";
    public String password = "1111";

    @Test
    @DisplayName("회원가입_정상")
    void signUp_정상() throws Exception {

        given(this.spec)
                .filter(documentSignUp())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(username, password))
        .when()
                .port(port)
                .post("/api/sign-up")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("$", hasKey("id"));
    }

    @Test
    @DisplayName("회원가입_실패_username_중복")
    void signUp_실패_username_중복() throws Exception {

        accountTestUtils.signUpUser(username, password);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(username, password))
        .when()
                .port(port)
                .post("/api/sign-up")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인_정상")
    void signin_정상() throws Exception {

        accountTestUtils.signUpUser(username, password);

        given(this.spec)
                .filter(documentSignIn())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(username, password))
        .when()
                .port(port)
                .post("/api/sign-in")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("$", hasKey("id"));
    }

    @Test
    @DisplayName("로그인_실패_username_없음")
    void signin_실패_username_없음() throws Exception {

        accountTestUtils.signUpUser(username, password);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto("failure", password))
        .when()
                .port(port)
                .post("/api/sign-in")
        .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }


    @Test
    @DisplayName("로그인_실패_password_틀림")
    void signin_실패_username_틀림() throws Exception {

        accountTestUtils.signUpUser(username, password);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(username, "failure"))
        .when()
                .port(port)
                .post("/api/sign-in")
        .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}