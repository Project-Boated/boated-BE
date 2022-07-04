package com.projectboated.backend.web.project.controller.document;

import io.restassured.filter.Filter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectDocument {

    public static RestDocumentationFilter documentProjectCreate() {
        return document("project-create",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("프로젝트 설명"),
                        fieldWithPath("deadline").type(JsonFieldType.STRING).description("프로젝트 기한 (yyyy-MM-dd HH:mm:ss)")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("만들어진 project의 id")
                )
        );
    }

    public static RestDocumentationFilter documentProjectUpdate() {
        return document("project-update",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("업데이트할 프로젝트 이름(생략가능)"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("업데이트할 프로젝트 설명(생략가능)"),
                        fieldWithPath("deadline").type(JsonFieldType.STRING).description("업데이트할 마감기한 (생략가능)(yyyy-MM-dd HH:mm:ss)")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("수정한 project의 id")
                )
        );
    }

    public static RestDocumentationFilter documentProjectDelete() {
        return document("project-delete",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유번호")
                )
        );
    }

    public static RestDocumentationFilter documentProjectTerminate() {
        return document("project-terminate",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로젝트 고유 id")
                )
        );
    }

    public static RestDocumentationFilter documentProjectCancelTerminate() {
        return document("project-cancel-terminate",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로젝트 고유 id")
                )
        );
    }

    public static RestDocumentationFilter documentProjectRetrieve() {
        return document("project-retrieve",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType")
                ),
                pathParameters(
                        parameterWithName("projectId").description("프로젝트의 id")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로젝트 id"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("프로젝트 설명"),
                        fieldWithPath("deadline").type(JsonFieldType.STRING).description("프로젝트 마감기한"),
                        fieldWithPath("captain").type(JsonFieldType.OBJECT).description("Captain정보"),
                        fieldWithPath("captain.id").type(JsonFieldType.NUMBER).description("Captain 고유id"),
                        fieldWithPath("captain.nickname").type(JsonFieldType.STRING).description("Captain 닉네임"),
                        fieldWithPath("terminated").type(JsonFieldType.BOOLEAN).description("종료된 프로젝트이면 true, 아니면 false"),
                        fieldWithPath("dday").type(JsonFieldType.NUMBER).description("dday"),
                        fieldWithPath("totalDay").type(JsonFieldType.NUMBER).description("프로젝트 수행시간")
                )
        );
    }

}
