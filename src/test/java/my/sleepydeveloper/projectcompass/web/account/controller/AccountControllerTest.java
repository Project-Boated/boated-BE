package my.sleepydeveloper.projectcompass.web.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        // 정상적인 회원가입
        given(this.spec)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new AccountDto(username, password, nickname))
        .when()
            .port(port)
            .post("/api/account/sign-up")
        .then()
            .statusCode(HttpStatus.CREATED.value());

        // 중복된 username으로 가입
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
    @DisplayName("AccountProfile_조회_성공")
    @WithMockJsonUser(username = username, nickname = nickname)
    void getAccountProfile_자신의profile가져오기_성공() throws Exception {

        mockMvc.perform(get("/api/account/profile")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andDo(documentAccountProfileRetrieve());
    }

    @Test
    @DisplayName("UpdateAccountProfile_성공")
    @WithMockJsonUser(password = password)
    void updateAccountProfile_성공() throws Exception {

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