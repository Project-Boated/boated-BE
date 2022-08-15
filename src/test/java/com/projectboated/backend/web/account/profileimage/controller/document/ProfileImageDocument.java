package com.projectboated.backend.web.account.profileimage.controller.document;

import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;

@DisplayName("Invitation : Controller 단위테스트")
public abstract class ProfileImageDocument {

    public static RestDocumentationResultHandler documentAccountProfileImageUpdate() {
        return document("account-profile-image-update",
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낼 Media Type")
                ),
                requestParts(
                        partWithName("file").description("바꿀 프로필 파일")
                )
        );
    }

    public static RestDocumentationResultHandler documentAccountProfileImageRetrieve() {
        return document("account-profile-image-retrieve");
    }

    public static RestDocumentationResultHandler documentAccountProfileImageDelete() {
        return document("account-profile-image-delete");
    }

}
