package com.projectboated.backend.web.account.controller;

import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.web.account.dto.PutNicknameRequest;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import com.projectboated.backend.infra.aws.AwsS3File;
import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.domain.profileimage.service.AwsS3ProfileImageService;
import com.projectboated.backend.web.account.dto.SignUpRequest;
import com.projectboated.backend.web.account.dto.ValidationNicknameUniqueRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static com.projectboated.backend.web.account.controller.document.AccountDocument.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest extends AcceptanceTest {

    @MockBean
    AwsS3ProfileImageService awsS3ProfileService;

    @Test
    void signUp_회원가입_성공() throws Exception {

        given(this.spec)
                .filter(documentSignUp())
                .contentType(ContentType.JSON)
                .body(new SignUpRequest(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL))
        .when()
                .port(port)
                .post("/api/account/sign-up")
        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void getAccountProfile_자신의profile가져오기_성공() throws Exception {

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);

        given(this.spec)
                .filter(documentAccountProfileRetrieve())
                .accept(ContentType.JSON)
                .cookie(cookie)
        .when()
                .port(port)
                .get("/api/account/profile")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("username", equalTo(BasicAccountData.USERNAME))
                .assertThat().body("nickname", equalTo(BasicAccountData.NICKNAME))
                .assertThat().body("profileImageUrl", startsWith(BasicAccountData.PROFILE_IMAGE_URL))
                .assertThat().body("roles", notNullValue());
    }

    @Test
    void patchAccountProfile_프로필update_성공() throws Exception {

        Mockito.when(awsS3ProfileService.uploadProfileImage(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn("http://test.com");

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);

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
                .multiPart("originalPassword", BasicAccountData.PASSWORD)
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
            .assertThat().body("username", equalTo(BasicAccountData.USERNAME))
            .assertThat().body("nickname", equalTo(updateNickname))
            .assertThat().body("profileImageUrl", notNullValue())
            .assertThat().body("roles", notNullValue());
    }

    @Test
    void deleteAccount_회원탈퇴_성공() throws Exception {

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);

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

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);

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

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);

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
    void postAccountProfileImage_프로필이미지수정_성공() throws IOException {
        Mockito.when(awsS3ProfileService.uploadProfileImage(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn("http://test.com");

        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);

        given(this.spec)
            .filter(documentAccountProfileImageUpdate())
            .accept(ContentType.JSON)
            .contentType(ContentType.MULTIPART)
            .cookie(cookie)
        .when()
            .port(port)
            .multiPart("file", ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg")
            .post("/api/account/profile/profile-image")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getAccountProfileImage_이미지Get_성공() throws IOException {
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);

        Mockito.when(awsS3ProfileService.getProfileImageBytes(Mockito.any()))
                .thenReturn(new AwsS3File("filename", "image/png", "asdf".getBytes()));

        given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.MULTIPART)
            .cookie(cookie)
        .when()
            .port(port)
            .multiPart("file", ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg")
            .post("/api/account/profile/profile-image");


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
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);

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