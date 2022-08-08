package com.projectboated.backend.web.task.taskfile.document;

import io.restassured.filter.Filter;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public abstract class TaskFileControllerDocument {

    public static Filter documentUploadTaskFile() {
        return document("tasks-files-create",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유번호"),
                        parameterWithName("taskId").description("task 고유번호")
                ),
                requestParts(
                        partWithName("file").description("업로드할 파일")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("파일의 고유번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("파일 이름"),
                        fieldWithPath("createdDate").type(JsonFieldType.STRING).description("저장한 날짜")
                )
        );
    }

}
