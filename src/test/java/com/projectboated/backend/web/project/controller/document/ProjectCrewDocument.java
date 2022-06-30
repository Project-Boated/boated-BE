package com.projectboated.backend.web.project.controller.document;

import io.restassured.filter.Filter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectCrewDocument {

    public static Filter documentProjectsCrewsRetrieve() {
        return document("project-crews-retrieve",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType")
                ),
                pathParameters(
                        parameterWithName("projectId").description("조회할 프로젝트 고유번호")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("crews").type(JsonFieldType.ARRAY).description("crew 목록"),
                        fieldWithPath("crews[].id").type(JsonFieldType.NUMBER).description("crew 고유번호"),
                        fieldWithPath("crews[].username").type(JsonFieldType.STRING).description("crew 아이디"),
                        fieldWithPath("crews[].nickname").type(JsonFieldType.STRING).description("crew 닉네임"),
                        fieldWithPath("crews[].profileImageUrl").type(JsonFieldType.STRING).description("crew 프로필 이미지 URL")
                )
        );
    }

    public static Filter documentProjectsCrewDelete() {
        return document("projects-crew-delete",
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트 고유번호"),
                        parameterWithName("crewNickname").description("방출할 crew의 nickname")
                )
        );
    }


}
