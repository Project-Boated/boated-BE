package com.projectboated.backend.utils.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projectboated.backend.account.account.service.AccountNicknameService;
import com.projectboated.backend.account.account.service.AccountService;
import com.projectboated.backend.account.profileimage.service.ProfileImageService;
import com.projectboated.backend.invitation.service.InvitationService;
import com.projectboated.backend.kanban.kanban.service.KanbanService;
import com.projectboated.backend.kanban.kanbanlane.service.KanbanLaneService;
import com.projectboated.backend.project.project.service.ProjectCaptainService;
import com.projectboated.backend.project.project.service.ProjectCrewService;
import com.projectboated.backend.project.project.service.ProjectService;
import com.projectboated.backend.project.project.service.ProjectTerminateService;
import com.projectboated.backend.project.projectchatting.chatting.service.ChattingService;
import com.projectboated.backend.project.projectchatting.chattingroom.service.ChattingRoomService;
import com.projectboated.backend.project.projectchatting.chattingroom.service.ProjectChattingRoomService;
import com.projectboated.backend.project.projectvideo.service.ProjectVideoService;
import com.projectboated.backend.task.task.service.AccountTaskService;
import com.projectboated.backend.task.task.service.TaskService;
import com.projectboated.backend.task.taskfile.service.TaskFileService;
import com.projectboated.backend.task.tasklike.service.TaskLikeService;
import com.projectboated.backend.infra.aws.AwsS3ProfileImageService;
import com.projectboated.backend.infra.aws.AwsS3Service;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.fail;

@WebMvcTest
@Import(ControllerTest.SecurityConfig.class)
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriHost = "project-boated.com")
public class ControllerTest extends BaseTest {

    @Autowired
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Account
    @MockBean
    protected AccountNicknameService accountNicknameService;
    @MockBean
    protected AccountService accountService;
    @MockBean
    protected ProfileImageService profileImageService;

    // Invitation
    @MockBean
    protected InvitationService invitationService;

    // Kanban
    @MockBean
    protected KanbanService kanbanService;
    @MockBean
    protected KanbanLaneService kanbanLaneService;

    // Project
    @MockBean
    protected ProjectService projectService;
    @MockBean
    protected ProjectCaptainService projectCaptainService;
    @MockBean
    protected ProjectCrewService projectCrewService;
    @MockBean
    protected ProjectTerminateService projectTerminateService;

    // Chatting
    @MockBean
    protected ChattingService chattingService;

    // Chatting Room
    @MockBean
    protected ChattingRoomService projectChattingService;
    @MockBean
    protected ProjectChattingRoomService projectChattingRoomService;

    // Task
    @MockBean
    protected TaskService taskService;
    @MockBean
    protected AccountTaskService accountTaskService;
    @MockBean
    protected TaskLikeService taskLikeService;
    @MockBean
    protected TaskFileService taskFileService;

    // Project Video
    @MockBean
    protected ProjectVideoService projectVideoService;

    // infra/aws
    @MockBean
    protected AwsS3Service awsS3Service;
    @MockBean
    protected AwsS3ProfileImageService awsS3ProfileImageService;

    // Socket message template
    @MockBean
    protected SimpMessagingTemplate simpMessagingTemplate;


    protected String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("json parsing 실패", e);
        }
        return null;
    }

    static class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/api/sign-in/kakao").permitAll()
                    .antMatchers("/api/account/sign-up").permitAll()
                    .antMatchers("/test/**").permitAll()
                    .anyRequest().authenticated();

            http.csrf()
                    .disable();

            http.requestCache()
                    .disable();

            return http.build();
        }
    }

}
