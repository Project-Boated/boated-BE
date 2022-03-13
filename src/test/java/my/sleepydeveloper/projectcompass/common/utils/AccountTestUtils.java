package my.sleepydeveloper.projectcompass.common.utils;

import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.security.dto.UsernamePasswordDto;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountDto;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static io.restassured.RestAssured.given;

@Service
public class AccountTestUtils {
    
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public AccountTestUtils(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    public Account signUpUser(String username, String password, String nickname) {
        return signUp(username, password, nickname, "ROLE_USER");
    }

    public Account signUp(String username, String password, String nickname, String role) {
        return accountService.save(new Account(username, passwordEncoder.encode(password), nickname, role));
    }

    public Account getAccountFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Account) authentication.getPrincipal();
    }

    public static void createAccount(int port, String username, String password, String nickname) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new AccountDto(username, password, nickname))
        .when()
            .port(port)
            .post("/api/account/sign-up");
    }

    public static Cookie login(int port, String username, String password) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new UsernamePasswordDto(username, password))
        .when()
            .port(port)
            .post("/api/sign-in")
        .thenReturn()
                .getDetailedCookie("JSESSIONID");
    }

}
