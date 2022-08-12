package com.projectboated.backend.web.account.account.controller.document;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class AccountDocument {

    public static RestDocumentationResultHandler documentSignUp() {
        return document("sign-up",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낼 Content Type")
                ),
                requestFields(
                        fieldWithPath("username").type(JsonFieldType.STRING).description("유저이름"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                )
        );
    }
    public static RestDocumentationResultHandler documentAccountProfileRetrieve() {
        return document("account-profile-retrieve",
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("username").type(JsonFieldType.STRING).description("유저 이름"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                        fieldWithPath("roles").type(JsonFieldType.ARRAY).description("권한")
                )
        );
    }

    public static RestDocumentationResultHandler documentAccountProfileUpdate() {
        return document("account-profile-update",
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낼 Media Type")
                ),
                requestParts(
                        partWithName("profileImageFile").description("바꿀 프로필 이미지 파일(생략가능)")
                ),
                requestParameters(
                        parameterWithName("nickname").description("바꿀 닉네임 (생략가능)"),
                        parameterWithName("originalPassword").description("원래 패스워드, 유저 검증을 위한 값(Oauth 로그인시 아무일도 안함)"),
                        parameterWithName("newPassword").description("바꿀 패스워드 (Oauth 로그인시 아무일도 안함) (생략가능)")
                )
        );
    }

    public static RestDocumentationResultHandler documentAccountDelete() {
        return document("account-profile-delete");
    }

}
