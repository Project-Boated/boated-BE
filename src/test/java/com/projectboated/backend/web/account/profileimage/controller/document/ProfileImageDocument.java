package com.projectboated.backend.web.account.profileimage.controller.document;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public abstract class ProfileImageDocument {

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
