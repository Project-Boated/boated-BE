package org.projectboated.backend.web.account.controller.document;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class AccountDocument {

    public static RestDocumentationFilter documentSignUp() {
        return document("sign-up",
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낼 Content Type")
                ),
                requestFields(
                        fieldWithPath("username").type(JsonFieldType.STRING).description("유저이름"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL (생략가능)").optional()
                )
        );
    }
    public static RestDocumentationFilter documentAccountProfileRetrieve() {
        return document("account-profile-retrieve",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("보낼 받을 타입")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Media Type")
                ),
                responseFields(
                        fieldWithPath("username").type(JsonFieldType.STRING).description("유저 이름"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                        fieldWithPath("roles").type(JsonFieldType.ARRAY).description("권한")
                )
        );
    }

    public static RestDocumentationFilter documentAccountProfileUpdate() {
        return document("account-profile-update",
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낼 Media Type")
                ),
                requestParts(
                        partWithName("nickname").description("바꿀 닉네임 (생략가능)"),
                        partWithName("originalPassword").description("원래 패스워드, 유저 검증을 위한 값(Oauth 로그인시 아무일도 안함)"),
                        partWithName("newPassword").description("바꿀 패스워드 (Oauth 로그인시 아무일도 안함) (생략가능)"),
                        partWithName("profileImageFile").description("바꿀 프로필 이미지 파일(생략가능)")
                )
        );
    }

    public static RestDocumentationFilter documentAccountDelete() {
        return document("account-profile-delete");
    }

    public static RestDocumentationFilter documentAccountNicknameUniqueValidation() {
        return document("account-nickname-validation",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 타입"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낼 Media Type")
                ),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("Validation하고 싶은 닉네임")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Media Type")
                ),
                responseFields(
                        fieldWithPath("duplicated").type(JsonFieldType.BOOLEAN).description("True면 이미 닉네임이 존재하는것. False면 없는 닉네임")
                )
        );
    }

    public static RestDocumentationFilter documentAccountPutNickname() {
        return document("account-nickname-update",
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낼 Media Type")
                ),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("바꾸고 싶은 닉네임")
                )
        );
    }

    public static RestDocumentationFilter documentAccountProfileImageUpdate() {
        return document("account-profile-image-update",
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낼 Media Type")
                ),
                requestParts(
                        partWithName("file").description("바꿀 프로필 파일")
                )
        );
    }

    public static RestDocumentationFilter documentAccountProfileImageDelete() {
        return document("account-profile-image-delete");
    }

    public static RestDocumentationFilter documentAccountProfileImageRetrieve() {
        return document("account-profile-image-retrieve");
    }


}
