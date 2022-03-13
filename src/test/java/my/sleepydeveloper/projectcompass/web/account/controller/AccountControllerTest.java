package my.sleepydeveloper.projectcompass.web.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.common.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.common.mock.WithMockJsonUser;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountDto;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.web.account.controller.document.AccountDocument.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends AcceptanceTest {

    @Autowired
    AccountTestUtils accountTestUtils;

    @Mock
    MockMvc mockMvc;

    final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void signUp_회원가입_성공() throws Exception {

        given(this.spec)
                .filter(documentSignUp())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new AccountDto(username, password, "newNickname"))
        .when()
                .port(port)
                .post("/api/account/sign-up")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockJsonUser(username = username, nickname = nickname)
    void getAccountProfile_자신의profile가져오기_Profile() throws Exception {
        AccountTestUtils.createAccount(port, username, password, nickname);
        Cookie cookie = AccountTestUtils.login(port, username, password);

        given(this.spec)
                .filter(documentAccountProfileRetrieve())
                .accept(MediaType.APPLICATION_JSON_VALUE)
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

        Account account = accountTestUtils.getAccountFromSecurityContext();

        mockMvc.perform(patch("/api/account/profile/" + account.getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new AccountUpdateRequest("updateNickname", password, "updatePassword"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andDo(documentAccountProfileUpdate());
    }

    @Test
    @DisplayName("UpdateAccountProfile_실패_권한없음")
    @WithMockJsonUser(password = password)
    void updateAccountProfile_실패_권한없음() throws Exception {

        mockMvc.perform(patch("/api/account/profile/" + "1234")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new AccountUpdateRequest("updateNickname", password, "updatePassword"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("deleteAccount_성공")
    @WithMockJsonUser
    void deleteAccount_성공() throws Exception {

        Account account = accountTestUtils.getAccountFromSecurityContext();

        mockMvc.perform(delete("/api/account/profile/" + account.getId())
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andDo(documentAccountDelete());
    }

    @Test
    @DisplayName("deleteAccount_실패_권한없음")
    @WithMockJsonUser
    void deleteAccount_실패_권한없음() throws Exception {

        mockMvc.perform(delete("/api/account/profile/" + "123")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("statusCode").value("U006"));
    }

}