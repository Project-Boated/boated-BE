package com.projectboated.backend.invitation.controller.document;

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
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class InvitationDocument {

    public static RestDocumentationResultHandler documentInvitationCreate() {
        return document("invitation-create",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 id")
                ),
                requestParameters(
                        parameterWithName("nickname").description("초대할 Account의 닉네임")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 invitation의 id")
                )
        );
    }

    public static RestDocumentationResultHandler documentMyInvitationRetrieve() {
        return document("invitation-my-retrieve",
                preprocessResponse(prettyPrint()),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("invitations").type(JsonFieldType.ARRAY).description("초대리스트"),
                        fieldWithPath("invitations[].id").type(JsonFieldType.NUMBER).description("프로젝트 고유 id"),
                        fieldWithPath("invitations[].createdDate").type(JsonFieldType.STRING).description("초대를 받은 날짜"),
                        fieldWithPath("invitations[].name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("invitations[].description").type(JsonFieldType.STRING).description("프로젝트 소개"),
                        fieldWithPath("invitations[].captainNickname").type(JsonFieldType.STRING).description("Captain 닉네임")
                )
        );
    }

    public static RestDocumentationResultHandler documentInvitationAccept() {
        return document("invitation-accept",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("invitationId").description("초대장 id")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Accept한 프로젝트 id")
                )
        );
    }

    public static RestDocumentationResultHandler documentInvitationReject() {
        return document("invitation-reject",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("invitationId").description("초대장 id")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Reject한 프로젝트 id")
                )
        );
    }

}
