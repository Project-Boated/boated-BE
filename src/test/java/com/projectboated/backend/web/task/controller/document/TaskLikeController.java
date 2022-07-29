package com.projectboated.backend.web.task.controller.document;

import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public abstract class TaskLikeController {

    public static RestDocumentationFilter documentTaskLike() {
        return document("tasks-like",
                pathParameters(
                        parameterWithName("projectId").description("찜할 프로젝트의 id"),
                        parameterWithName("taskId").description("찜할 task의 id")
                )
        );
    }

}
