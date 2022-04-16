package my.sleepydeveloper.projectcompass.web.account.controller;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.common.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.web.account.dto.SignUpRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountUpdateRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.NicknameUniqueValidationRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.PutNicknameRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.web.account.controller.document.AccountDocument.*;
import static org.hamcrest.Matchers.*;

class AccountControllerTest extends AcceptanceTest {

    @Autowired
    AccountTestUtils accountTestUtils;

    @Test
    void signUp_회원가입_성공() throws Exception {

        given(this.spec)
                .filter(documentSignUp())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new SignUpRequest(USERNAME, PASSWORD, NICKNAME))
        .when()
                .port(port)
                .post("/api/account/sign-up")
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .assertThat().body("$", hasKey("id"));
    }

    @Test
    void signUp_중복된username으로가입_오류발생() throws Exception {

        accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME);

        given(this.spec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new SignUpRequest(USERNAME, PASSWORD, "newNickname"))
        .when()
                .port(port)
                .post("/api/account/sign-up")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getAccountProfile_자신의profile가져오기_Profile() throws Exception {
        System.out.println(accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME));
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        given(this.spec)
                .filter(documentAccountProfileRetrieve())
                .accept(ContentType.JSON)
                .cookie(cookie)
        .when()
                .port(port)
                .get("/api/account/profile")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("username", equalTo(USERNAME))
                .assertThat().body("nickname", equalTo(NICKNAME))
                .assertThat().body("role", equalTo("ROLE_USER"));
    }

    @Test
    void updateAccountProfile_프로필update_update성공() throws Exception {

        accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        given(this.spec)
                .filter(documentAccountProfileUpdate())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
        .when()
                .port(port)
                .body(new AccountUpdateRequest("updateNickname", PASSWORD, "updatePassword", null))
                .patch("/api/account/profile")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("id", notNullValue());
    }

    @Test
    void deleteAccount_회원탈퇴_성공() throws Exception {

        accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        given(this.spec)
                .filter(documentAccountDelete())
                .accept(ContentType.JSON)
                .cookie(cookie)
        .when()
                .port(port)
                .delete("/api/account/profile")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("id", notNullValue());
    }

    @Test
    void nicknameUniqueValidation_중복되지않는닉네임조회_dupliatedTrue() throws Exception {

        accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        given(this.spec)
                .filter(documentAccountNicknameUniqueValidation())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new NicknameUniqueValidationRequest("notDuplicatedNickname"))
        .when()
                .port(port)
                .post("/api/account/profile/nickname/unique-validation")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("duplicated", equalTo(false));
    }

    @Test
    void putNickname_닉네임변경_200반환() throws Exception {

        accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        given(this.spec)
                .filter(documentAccountPutNickname())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new PutNicknameRequest("nickname2"))
        .when()
                .port(port)
                .put("/api/account/profile/nickname")
        .then()
                .statusCode(HttpStatus.OK.value());
    }

}