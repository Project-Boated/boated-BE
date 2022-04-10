package my.sleepydeveloper.projectcompass.web.account.controller;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.common.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountDto;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountUpdateRequest;
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
                .body(new AccountDto(username, password, nickname))
        .when()
                .port(port)
                .post("/api/account/sign-up")
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .assertThat().body("$", hasKey("id"));
    }

    @Test
    void signUp_중복된username으로가입_오류발생() throws Exception {

        AccountTestUtils.createAccount(port, username, password, nickname);

        given(this.spec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new AccountDto(username, password, "newNickname"))
        .when()
                .port(port)
                .post("/api/account/sign-up")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getAccountProfile_자신의profile가져오기_Profile() throws Exception {
        System.out.println(AccountTestUtils.createAccount(port, username, password, nickname));
        Cookie cookie = AccountTestUtils.login(port, username, password);

        given(this.spec)
                .filter(documentAccountProfileRetrieve())
                .accept(ContentType.JSON)
                .cookie(cookie)
        .when()
                .port(port)
                .get("/api/account/profile")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("username", equalTo(username))
                .assertThat().body("nickname", equalTo(nickname))
                .assertThat().body("role", equalTo("ROLE_USER"));
    }

    @Test
    void updateAccountProfile_프로필update_update성공() throws Exception {

        AccountTestUtils.createAccount(port, username, password, nickname);
        Cookie cookie = AccountTestUtils.login(port, username, password);

        given(this.spec)
                .filter(documentAccountProfileUpdate())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
        .when()
                .port(port)
                .body(new AccountUpdateRequest("updateNickname", password, "updatePassword"))
                .patch("/api/account/profile")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("id", notNullValue());
    }

    @Test
    void deleteAccount_회원탈퇴_성공() throws Exception {

        AccountTestUtils.createAccount(port, username, password, nickname);
        Cookie cookie = AccountTestUtils.login(port, username, password);

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

}