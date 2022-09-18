package com.projectboated.backend.project.controller.document;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public abstract class ProjectCaptainDocument {

    public static RestDocumentationResultHandler documentProjectCaptainUpdate() {
        return document("project-captain-update",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 고유번호")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낼 Content Type")
                ),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("바꾸고 싶은 captain의 nickname")
                )
        );
    }

}
