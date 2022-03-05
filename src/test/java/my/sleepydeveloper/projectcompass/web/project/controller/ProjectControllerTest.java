package my.sleepydeveloper.projectcompass.web.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.sleepydeveloper.projectcompass.common.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.common.data.AccountBasicData;
import my.sleepydeveloper.projectcompass.common.mock.WithMockJsonUser;
import my.sleepydeveloper.projectcompass.common.utils.AccountProjectTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.ProjectTestUtils;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.service.ProjectService;
import my.sleepydeveloper.projectcompass.web.project.dto.InviteCrewRequest;
import my.sleepydeveloper.projectcompass.web.project.dto.ProjectSaveRequest;
import my.sleepydeveloper.projectcompass.web.project.dto.ProjectUpdateRequest;
import my.sleepydeveloper.projectcompass.web.project.dto.UpdateCaptainRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static my.sleepydeveloper.projectcompass.common.data.AccountBasicData.*;
import static my.sleepydeveloper.projectcompass.common.data.ProjectBasicData.*;
import static my.sleepydeveloper.projectcompass.web.project.controller.document.ProjectDocument.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ProjectControllerTest extends AcceptanceTest {

    @Autowired
    AccountTestUtils accountTestUtils;

    @Autowired
    ProjectTestUtils projectTestUtils;

    @Autowired
    AccountProjectTestUtils accountProjectTestUtils;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProjectService projectService;

    final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("save_정상")
    @WithMockJsonUser
    void save_정상() throws Exception {
        // Given
        Account account = accountTestUtils.getAccountFromSecurityContext();

        // When
        // Then
        mockMvc.perform(post("/api/projects")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ProjectSaveRequest(projectName, projectDescription))))
                .andExpect(status().isOk())
                .andDo(documentProjectCreate());
    }

    @Test
    @DisplayName("save_실패_같은accunt에_같은name")
    @WithMockJsonUser
    void save_실패_같은account에_같은name() throws Exception {
        // Given
        Account account = accountTestUtils.getAccountFromSecurityContext();

        projectService.save(new Project(projectName, projectDescription, account));

        // When
        // Then
        mockMvc.perform(post("/api/projects")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ProjectSaveRequest(projectName, projectDescription))))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("update_정상")
    @WithMockJsonUser
    void update_정상() throws Exception {
        // Given
        Account account = accountTestUtils.getAccountFromSecurityContext();
        Project project = projectTestUtils.createProject(account);

        // When
        // Then
        mockMvc.perform(patch("/api/projects/" + project.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProjectUpdateRequest(updateProjectName, updateProjectDescription))))
                .andExpect(status().isOk())
                .andDo(documentProjectUpdate());
    }

    @Test
    @DisplayName("myProject_정상")
    @WithMockJsonUser
    void myProject_정상() throws Exception {
        // Given
        Account account = accountTestUtils.getAccountFromSecurityContext();
        for (int i = 0; i < 3; i++) {
            projectTestUtils.createProject(account, projectName + i, projectDescription);
        }

        // When
        // Then
        mockMvc.perform(get("/api/projects/my")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(documentProjectMy());
    }

    @Test
    @DisplayName("getCrews_정상")
    @WithMockJsonUser
    void getCrews_정상() throws Exception {
        // Given
        Account account = accountTestUtils.getAccountFromSecurityContext();
        Project project = projectTestUtils.createProject(account);
        AccountProject accountProject = accountProjectTestUtils.createAccountRepository(account, project);

        // When
        // Then
        mockMvc.perform(get("/api/projects/" + project.getId() + "/crews")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("crews").exists())
                .andExpect(jsonPath("crews[0].username").value(account.getUsername()))
                .andExpect(jsonPath("crews[0].nickname").value(account.getNickname()))
                .andDo(documentProjectRetrieveCrews());
    }

    @Test
    @DisplayName("updateCaptain_정상")
    @WithMockJsonUser
    void updateCaptain_정상() throws Exception {
        // Given
        Account captain = accountTestUtils.getAccountFromSecurityContext();
        Project project = projectTestUtils.createProject(captain, projectName, projectDescription);
        String newCaptainUsername = "newCaptain";
        String newCaptainPassword = "newCaptain";
        String newCaptainNickname = "newCaptain";
        Account newCaptain = accountTestUtils.signUp(newCaptainUsername, newCaptainPassword, newCaptainNickname, "ROLE_USER");
        AccountProject accountProject = accountProjectTestUtils.createAccountRepository(newCaptain, project);

        // When
        // Then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/projects/{projectId}/captain", project.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateCaptainRequest(newCaptainUsername))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(newCaptain.getId()))
                .andDo(documentProjectUpdateCaptain());
    }

    @Test
    @DisplayName("inviteCrew_정상")
    @WithMockJsonUser
    void inviteCrew_정상() throws Exception {
        // Given
        Account captain = accountTestUtils.getAccountFromSecurityContext();
        Project project = projectTestUtils.createProject(captain, projectName, projectDescription);
        String crewUsername = "crew";
        String crewNickname = "crew";
        Account crew = accountTestUtils.signUpUser(crewUsername, password, crewNickname);

        // When
        // Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/projects/{projectId}/crews", project.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new InviteCrewRequest(crewUsername))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andDo(documentProjectInviteCrew());
    }
}