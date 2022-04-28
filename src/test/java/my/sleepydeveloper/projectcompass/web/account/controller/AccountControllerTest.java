package my.sleepydeveloper.projectcompass.web.account.controller;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.aws.AwsS3File;
import my.sleepydeveloper.projectcompass.aws.AwsS3Service;
import my.sleepydeveloper.projectcompass.common.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.web.account.dto.PutNicknameRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.SignUpRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.ValidationNicknameUniqueRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.web.account.controller.document.AccountDocument.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest extends AcceptanceTest {

    @MockBean
    AwsS3Service awsS3Service;

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
    void patchAccountProfile_프로필update_성공() throws Exception {

        Mockito.when(awsS3Service.uploadProfileImage(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn("http://test.com");

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        String updateNickname = "updateNickname";
        String updatePassword = "updatePassword";

        // Account Update
        given(this.spec)
                .filter(documentAccountProfileUpdate())
                .accept(ContentType.JSON)
                .contentType(ContentType.MULTIPART)
                .cookie(cookie)
        .when()
                .port(port)
                .multiPart("nickname", updateNickname)
                .multiPart("originalPassword", PASSWORD)
                .multiPart("newPassword", updatePassword)
                .multiPart("profileImageFile", ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg")
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
            .assertThat().body("profileImageUrl", equalTo("host"))
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
    void validationNicknameUnique_중복되지않는닉네임조회_notDupliated() throws Exception {

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        given(this.spec)
                .filter(documentAccountNicknameUniqueValidation())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new ValidationNicknameUniqueRequest("notDuplicatedNickname"))
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

    @Test
    void putAccountProfileImage_프로필이미지수정_성공() throws IOException {
        Mockito.when(awsS3Service.uploadProfileImage(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn("http://test.com");

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        given(this.spec)
            .filter(documentAccountProfileImageUpdate())
            .accept(ContentType.JSON)
            .contentType(ContentType.MULTIPART)
            .cookie(cookie)
        .when()
            .port(port)
            .multiPart("file", ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg")
            .put("/api/account/profile/profile-image")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getAccountProfileImage_이미지Get_성공() throws IOException {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        Mockito.when(awsS3Service.getProfileImageBytes(Mockito.any()))
                .thenReturn(new AwsS3File("asdf".getBytes(), "image/png", "filename"));

        given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.MULTIPART)
            .cookie(cookie)
        .when()
            .port(port)
            .multiPart("file", ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg")
            .put("/api/account/profile/profile-image");


        given(this.spec)
            .filter(documentAccountProfileImageRetrieve())
            .cookie(cookie)
        .when()
            .port(port)
            .get("/api/account/profile/profile-image")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteAccountProfileImage_이미지삭제_성공() throws IOException {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        given(this.spec)
            .filter(documentAccountProfileImageDelete())
            .accept(ContentType.JSON)
            .cookie(cookie)
        .when()
            .port(port)
            .delete("/api/account/profile/profile-image")
        .then()
            .statusCode(HttpStatus.OK.value());
    }


}