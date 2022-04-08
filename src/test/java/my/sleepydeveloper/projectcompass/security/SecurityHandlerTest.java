package my.sleepydeveloper.projectcompass.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import my.sleepydeveloper.projectcompass.security.code.SecurityHandlerTestConfig;
import my.sleepydeveloper.projectcompass.security.mock.WithMockJsonUser;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityHandlerTestConfig.class)
@ActiveProfiles("oauth")
public class SecurityHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockJsonUser
    public void accessDeniedHandler_test() throws Exception {
        // 권한 없는 곳으로 요청
        mockMvc.perform(get("/test/access-denied"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void authenticationEntryPoint_test() throws Exception {
        // 로그인 필요한 곳으로 요청
        mockMvc.perform(get("/test/access-denied"))
                .andExpect(status().isUnauthorized());
    }

}
