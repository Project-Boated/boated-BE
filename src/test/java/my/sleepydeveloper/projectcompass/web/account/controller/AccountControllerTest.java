package my.sleepydeveloper.projectcompass.web.account.controller;

import my.sleepydeveloper.projectcompass.security.dto.UsernamePasswordDto;
import my.sleepydeveloper.projectcompass.test.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.test.mock.WithMockJsonUser;
import my.sleepydeveloper.projectcompass.test.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcBuilderCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.web.account.controller.documentfilter.AccountDocument.documentSignUp;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
class AccountControllerTest extends AcceptanceTest {

    @Autowired
    AccountTestUtils accountTestUtils;

    @Autowired
    MockMvc mockMvc;

    public final String username = "username";
    public final String password = "password";
    private final String nickname = "user";

    @Test
    @DisplayName("회원가입_정상")
    void signUp_정상() throws Exception {

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
    @DisplayName("회원가입_실패_username_중복")
    void signUp_실패_username_중복() throws Exception {

        accountTestUtils.signUpUser(username, password, nickname);

        given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UsernamePasswordDto(username, password))
        .when()
                .port(port)
                .post("/api/account/sign-up")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("AccountProfile_조회_성공")
    @WithMockJsonUser
    void getAccountProfile_성공() throws Exception {

        accountTestUtils.signUpUser(username, password, nickname);

        mockMvc.perform(get("/api/account/profile")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andDo(document("account-retrieve-profile",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("받을 타입")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Media Type")
                        ),
                        responseFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("role").type(JsonFieldType.STRING).description("권한")
                        )
                ));
    }

}