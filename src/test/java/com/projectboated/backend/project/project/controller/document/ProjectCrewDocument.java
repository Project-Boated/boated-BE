package com.projectboated.backend.project.project.controller.document;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public abstract class ProjectCrewDocument {

    public static RestDocumentationResultHandler documentProjectsCrewsRetrieve() {
        return document("project-crews-retrieve",
                preprocessResponse(prettyPrint()),
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
                        fieldWithPath("crews[].profileImageUrl").description("crew 프로필 이미지 URL")
                )
        );
    }

    public static RestDocumentationResultHandler documentProjectsCrewDelete() {
        return document("projects-crew-delete",
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트 고유번호"),
                        parameterWithName("crewNickname").description("방출할 crew의 nickname")
                )
        );
    }


}
