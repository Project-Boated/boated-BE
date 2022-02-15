package com.example.projectcompass.web.account.documentfilter;

import org.springframework.http.HttpHeaders;
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
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원가입된 id")
                )
        );
    }
    
    public static RestDocumentationFilter documentSignIn() {
        return document("sign-in",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                requestFields(
                        fieldWithPath("username").type(JsonFieldType.STRING).description("유저이름"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("로그인된 id")
                )
        );
    }

}
