package com.projectboated.backend.web.projectvideo.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public abstract class ProjectVideoDocument {

    public static RestDocumentationResultHandler documentProjectVideoCreate() {
        return document("project-video-create",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 고유번호")
                ),
                requestParts(
                        partWithName("file").description("파일")
                )
        );
    }
}
