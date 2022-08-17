package com.projectboated.backend.web.task.task.document;


import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public abstract class TaskDocument {

    public static RestDocumentationResultHandler documentTaskCreate() {
        return document("tasks-create",
                preprocessResponse(prettyPrint()),
                requestHeaders(
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

    public static RestDocumentationResultHandler documentTaskAssign() {
        return document("tasks-assign",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 고유번호"),
                        parameterWithName("taskId").description("target task의 고유번호")
                ),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("assign할 account의 nickname")
                )
        );
    }

    public static RestDocumentationResultHandler documentTaskCancelAssign() {
        return document("tasks-assign-cancel",
                preprocessResponse(prettyPrint()),
                requestHeaders(
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

    public static RestDocumentationResultHandler documentTaskRetrieve() {
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

    public static RestDocumentationResultHandler documentTaskUpdate() {
        return document("tasks-update",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 id"),
                        parameterWithName("taskId").description("target task의 id")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름").optional(),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("설명").optional(),
                        fieldWithPath("deadline").type(JsonFieldType.STRING).description("deadline").optional(),
                        fieldWithPath("laneId").type(JsonFieldType.NUMBER).description("다른 lane").optional()
                )
        );
    }

    public static RestDocumentationResultHandler documentTaskDelete() {
        return document("tasks-delete",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 고유번호"),
                        parameterWithName("taskId").description("target task의 고유번호")
                )
        );
    }

    public static RestDocumentationResultHandler documentTaskOrderChange() {
        return document("kanban-lanes-tasks-order-change",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유 번호"),
                        parameterWithName("originalLaneId").description("바꾸고 싶은 lane의 고유번호"),
                        parameterWithName("originalTaskIndex").description("바꾸고 싶은 task의 index(index번호입니다. id아닙니다)"),
                        parameterWithName("changeLaneId").description("바꿔지게 될 lane의 고유번호"),
                        parameterWithName("changeTaskIndex").description("바꿔지게 될 task의 index(index번호입니다. id아닙니다)")
                )
        );
    }
}
