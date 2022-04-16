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

    @Test
    void signUp_회원가입_성공() throws Exception {

        given(this.spec)
                .filter(documentSignUp())
                .contentType(ContentType.JSON)
                .body(new SignUpRequest(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL))
        .when()
                .port(port)
                .post("/api/account/sign-up")
        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void getAccountProfile_자신의profile가져오기_성공() throws Exception {

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
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
                .assertThat().body("profileImageUrl", equalTo(PROFILE_IMAGE_URL))
                .assertThat().body("roles", notNullValue());
    }

    @Test
    void updateAccountProfile_프로필update_성공() throws Exception {

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        String updateNickname = "updateNickname";
        String updatePassword = "updatePassword";
        String updateProfileImageUrl = "updateProfileImageUrl";

        // Account Update
        given(this.spec)
                .filter(documentAccountProfileUpdate())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
        .when()
                .port(port)
                .body(new AccountUpdateRequest(updateNickname, PASSWORD, updatePassword, updateProfileImageUrl))
                .patch("/api/account/profile")
        .then()
                .statusCode(HttpStatus.OK.value());

        // 제대로 update 됐는지 확인
        given(this.spec)
            .accept(ContentType.JSON)
            .cookie(cookie)
        .when()
            .port(port)
            .get("/api/account/profile")
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("username", equalTo(USERNAME))
            .assertThat().body("nickname", equalTo(updateNickname))
            .assertThat().body("profileImageUrl", equalTo(updateProfileImageUrl))
            .assertThat().body("roles", notNullValue());
    }

    @Test
    void deleteAccount_회원탈퇴_성공() throws Exception {

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        given(this.spec)
                .filter(documentAccountDelete())
                .accept(ContentType.JSON)
                .cookie(cookie)
        .when()
                .port(port)
                .delete("/api/account/profile")
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void nicknameUniqueValidation_중복되지않는닉네임조회_notDupliated() throws Exception {

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
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
    void putNickname_닉네임변경_성공() throws Exception {

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
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