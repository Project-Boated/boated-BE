package com.projectboated.backend.web.task.task.document;


import io.restassured.filter.Filter;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public abstract class TaskDocument {

    public static RestDocumentationFilter documentTaskCreate() {
        return document("tasks-create",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 id")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("Task 이름"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("Task 설명"),
                        fieldWithPath("deadline").type(JsonFieldType.STRING).description("마감기한 (yyyy-MM-dd HH:mm:ss)")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 invitation의 id")
                )
        );
    }

    public static RestDocumentationFilter documentTaskAssign() {
        return document("tasks-assign",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 id"),
                        parameterWithName("taskId").description("target task의 id")
                ),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("assign할 account의 nickname")
                )
        );
    }

    public static RestDocumentationFilter documentTaskCancelAssign() {
        return document("tasks-assign-cancel",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 id"),
                        parameterWithName("taskId").description("target task의 id")
                ),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("assign 취소할 account의 nickname")
                )
        );
    }

    public static RestDocumentationFilter documentTaskRetrieve() {
        return document("tasks-retrieve",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 id"),
                        parameterWithName("taskId").description("target task의 id")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("task 고유번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("task 이름"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("task 설명"),
                        fieldWithPath("deadline").type(JsonFieldType.STRING).description("deadline"),
                        fieldWithPath("laneId").type(JsonFieldType.NUMBER).description("task가 속한 lane의 id"),
                        fieldWithPath("assignedAccounts").type(JsonFieldType.ARRAY).description("배정된 account"),
                        fieldWithPath("assignedAccounts[].id").type(JsonFieldType.NUMBER).description("배정된 account의 고유번호"),
                        fieldWithPath("assignedAccounts[].nickname").type(JsonFieldType.STRING).description("배정된 account의 닉네임"),
                        fieldWithPath("assignedFiles").type(JsonFieldType.ARRAY).description("배정된 파일들"),
                        fieldWithPath("assignedFiles[].id").type(JsonFieldType.NUMBER).description("파일의 고유번호"),
                        fieldWithPath("assignedFiles[].name").type(JsonFieldType.STRING).description("파일의 이름"),
                        fieldWithPath("assignedFiles[].createdDate").type(JsonFieldType.STRING).description("파일이 생성된 날짜")
                )
        );
    }
}
