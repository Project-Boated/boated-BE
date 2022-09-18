package com.projectboated.backend.project.controller.document;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public abstract class ProjectDocument {

    public static RestDocumentationResultHandler documentProjectCreate() {
        return document("project-create",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("프로젝트 설명"),
                        fieldWithPath("deadline").type(JsonFieldType.STRING).description("프로젝트 기한 (yyyy-MM-dd HH:mm:ss)").optional()
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("만들어진 project의 id")
                )
        );
    }

    public static RestDocumentationResultHandler documentProjectUpdate() {
        return document("project-update",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("업데이트할 프로젝트 이름").optional(),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("업데이트할 프로젝트 설명").optional(),
                        fieldWithPath("deadline").type(JsonFieldType.STRING).description("업데이트할 마감기한 (yyyy-MM-dd HH:mm:ss)").optional()
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("수정한 project의 id")
                )
        );
    }

    public static RestDocumentationResultHandler documentProjectDelete() {
        return document("project-delete",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유번호")
                )
        );
    }

    public static RestDocumentationResultHandler documentProjectRetrieve() {
        return document("project-retrieve",
                preprocessResponse(prettyPrint()),
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
                        fieldWithPath("totalDay").type(JsonFieldType.NUMBER).description("프로젝트 수행시간"),
                        fieldWithPath("taskSize").type(JsonFieldType.NUMBER).description("총 task 개수"),
                        fieldWithPath("totalFileSize").type(JsonFieldType.NUMBER).description("현재 프로젝트가 가진 총 파일 Size, byte단위(단위는 언제든지 바뀔 수 있음)")
                )
        );
    }

}
