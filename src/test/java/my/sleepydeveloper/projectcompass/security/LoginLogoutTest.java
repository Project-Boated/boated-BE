package my.sleepydeveloper.projectcompass.security;

import my.sleepydeveloper.projectcompass.common.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.security.dto.UsernamePasswordDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.security.document.LoginLogoutDocument.documentSignIn;
import static org.hamcrest.Matchers.hasKey;

public class LoginLogoutTest extends AcceptanceTest {

    @Autowired
    AccountTestUtils accountTestUtils;

    @Test
    @DisplayName("로그인_정상")
    void signin_정상() throws Exception {

        accountTestUtils.signUpUser(username, password, nickname);

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

        accountTestUtils.signUpUser(username, password, nickname);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(null, password))
                .when()
                .port(port)
                .post("/api/sign-in")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인_실패_password_없음")
    void signin_실패_password_없음() throws Exception {

        accountTestUtils.signUpUser(username, password, nickname);

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

        accountTestUtils.signUpUser(username, password, nickname);

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

        accountTestUtils.signUpUser(username, password, nickname);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto("failure", password))
                .when()
                .port(port)
                .post("/api/sign-in")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    @DisplayName("로그인_실패_password_틀림")
    void signin_실패_password_틀림() throws Exception {

        accountTestUtils.signUpUser(username, password, nickname);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(username, "failure"))
                .when()
                .port(port)
                .post("/api/sign-in")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인_실패_잘못된_MediaType")
    void signin_실패_잘못된_헤더() throws Exception {

        accountTestUtils.signUpUser(username, password, nickname);

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

        accountTestUtils.signUpUser(username, password, nickname);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(username, password))
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
