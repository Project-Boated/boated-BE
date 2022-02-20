package my.sleepydeveloper.projectcompass.web.account.controller.document;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class AccountDocument {

    public static RestDocumentationFilter documentSignUp() {
        return document("sign-up",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                requestFields(
                        fieldWithPath("username").type(JsonFieldType.STRING).description("유저이름"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type"),
                        headerWithName(HttpHeaders.LOCATION).description("회원가입된 유저의 프로필 URL")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원가입된 id")
                )
        );
    }
    public static RestDocumentationResultHandler documentGetProfile() {
        return MockMvcRestDocumentation.document("account-retrieve-profile",
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
        );
    }

    public static RestDocumentationResultHandler documentAccountUpdateProfile() {
        return MockMvcRestDocumentation.document("account-update-profile",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 타입"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Media Type")
                ),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("바꿀 이름(없어도 됨)"),
                        fieldWithPath("originalPassword").type(JsonFieldType.STRING).description("원래 패스워드, 유저 검증을 위한 값(필수값)"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("바꿀 패스워드(없어도 됨)")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Media Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("바꾼 Account id")
                )
        );
    }

    public static RestDocumentationResultHandler documentAccountDelete() {
        return MockMvcRestDocumentation.document("account-delete-profile",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 타입")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Media Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Delete된 Account id")
                )
        );
    }



}
