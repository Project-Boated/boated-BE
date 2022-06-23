package org.projectboated.backend.security;

import org.projectboated.backend.common.basetest.AcceptanceTest;
import org.projectboated.backend.common.utils.AccountTestUtils;
import org.projectboated.backend.security.dto.UsernamePasswordDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.projectboated.backend.common.data.BasicAccountData;
import org.projectboated.backend.security.document.LoginLogoutDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

@Disabled
public class LoginLogoutTest extends AcceptanceTest {

    @Test
    @DisplayName("로그인_정상")
    void signin_정상() throws Exception {

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);

        given(this.spec)
            .filter(LoginLogoutDocument.documentSignIn())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new UsernamePasswordDto(BasicAccountData.USERNAME, BasicAccountData.PASSWORD))
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

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(null, BasicAccountData.PASSWORD))
                .when()
                .port(port)
                .post("/api/sign-in")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인_실패_password_없음")
    void signin_실패_password_없음() throws Exception {

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto("failure", null))
                .when()
                .port(port)
                .post("/api/sign-in")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인_실패_아무것도_없음")
    void signin_실패_아무것도_없음() throws Exception {

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(null, null))
                .when()
                .port(port)
                .post("/api/sign-in")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인_실패_username_틀림")
    void signin_실패_username_틀림() throws Exception {

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto("failure", BasicAccountData.PASSWORD))
                .when()
                .port(port)
                .post("/api/sign-in")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    @DisplayName("로그인_실패_password_틀림")
    void signin_실패_password_틀림() throws Exception {

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(BasicAccountData.USERNAME, "failure"))
                .when()
                .port(port)
                .post("/api/sign-in")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인_실패_잘못된_MediaType")
    void signin_실패_잘못된_헤더() throws Exception {

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.TEXT_HTML_VALUE)
                .body("{\"username\" : \"username\", \"password\" : \"password\"}")
                .when()
                .port(port)
                .post("/api/sign-in")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인_실패_잘못된_Method")
    void signin_실패_잘못된_Method() throws Exception {

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(BasicAccountData.USERNAME, BasicAccountData.PASSWORD))
                .when()
                .port(port)
                .get("/api/sign-in")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_성공() throws Exception {

        // 로그인
        signin_정상();

        // 로그아웃
        given(this.spec)
                .when()
                .port(port)
                .post("/api/logout")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

}
