package my.sleepydeveloper.projectcompass.web.account.controller;

import my.sleepydeveloper.projectcompass.security.dto.UsernamePasswordDto;
import my.sleepydeveloper.projectcompass.test.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.test.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.web.account.controller.documentfilter.AccountDocument.documentSignUp;
import static org.hamcrest.Matchers.hasKey;


class AccountControllerTest extends AcceptanceTest {

    @Autowired
    AccountTestUtils accountTestUtils;

    public final String username = "user";
    public final String password = "1111";
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
                .post("/api/sign-up")
        .then()
                .statusCode(HttpStatus.OK.value())
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
                .post("/api/sign-up")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}