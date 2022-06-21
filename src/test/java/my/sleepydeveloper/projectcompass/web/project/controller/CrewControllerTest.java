package my.sleepydeveloper.projectcompass.web.project.controller;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.common.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.common.utils.AccountProjectTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.InvitationTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.ProjectTestUtils;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.accountproject.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.web.project.dto.CreateProjectRequest;
import my.sleepydeveloper.projectcompass.web.project.dto.GetCrewsResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.MockitoMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.*;
import static my.sleepydeveloper.projectcompass.web.project.controller.document.ProjectDocument.documentProjectCreate;
import static my.sleepydeveloper.projectcompass.web.project.controller.document.ProjectDocument.documentProjectRetrieveCrews;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CrewControllerTest extends AcceptanceTest {

    final String CREW_USERNAME = "crewUsername";
    final String CREW_NICKNAME = "crewUsername";

    @Autowired
    MockMvc mockMvc;

    @Test
    void getCrews_project에속한모든crew조회_성공() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, CREW_USERNAME, PASSWORD, CREW_NICKNAME, PROFILE_IMAGE_URL);

        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        Cookie crewCookie = AccountTestUtils.login(port, CREW_USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, CREW_NICKNAME);

        long invitationId = InvitationTestUtils.getMyInvitations(port, crewCookie).getBody().jsonPath().getLong("invitations[0].id");
        InvitationTestUtils.acceptInvitation(port, crewCookie, invitationId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectRetrieveCrews())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
            .get("/api/projects/{projectId}/crews", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}