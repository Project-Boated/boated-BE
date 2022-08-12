package com.projectboated.backend.web.account.account.controller.document;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public abstract class AccountNicknameDocument {

    public static RestDocumentationResultHandler documentAccountPutNickname() {
        return document("account-nickname-update",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낼 Media Type")
                ),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("바꾸고 싶은 닉네임")
                )
        );
    }

    public static RestDocumentationResultHandler documentAccountNicknameUniqueValidation() {
        return document("account-nickname-validation",
                preprocessResponse(prettyPrint()),
                requestHeaders(
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

}
