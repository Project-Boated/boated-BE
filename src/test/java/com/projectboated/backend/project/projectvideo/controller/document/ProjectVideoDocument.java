package com.projectboated.backend.project.projectvideo.controller.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public abstract class ProjectVideoDocument {

    public static RestDocumentationResultHandler documentProjectVideoCreate() {
        return document("project-video-put",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 고유번호")
                ),
                requestParts(
                        partWithName("file").description("파일")
                )
        );
    }

    public static RestDocumentationResultHandler documentProjectVideoRetrieve() {
        return document("project-video-retrieve",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 고유번호")
                )
        );
    }

    public static RestDocumentationResultHandler documentProjectVideoDelete() {
        return document("project-video-delete",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 고유번호")
                )
        );
    }

    public static RestDocumentationResultHandler documentProjectVideoDescriptionPut() {
        return document("project-video-description-put",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 고유번호")
                ),
                requestFields(
                        fieldWithPath("description").type(JsonFieldType.STRING).description("바꾸고 싶은 설명")
                )
        );
    }

    public static RestDocumentationResultHandler documentProjectVideoDescriptionGet() {
        return document("project-video-description-retrieve",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 고유번호")
                ),
                responseFields(
                        fieldWithPath("description").type(JsonFieldType.STRING).description("프로젝트 비디오의 설명")
                )
        );
    }
}
