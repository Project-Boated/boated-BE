package my.sleepydeveloper.projectcompass.security;

import my.sleepydeveloper.projectcompass.common.mock.WithMockJsonUser;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizeTest{

    @Autowired
    AccountTestUtils accountTestUtils;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("권한없음")
    @WithMockJsonUser
    void 권한없음() throws Exception {

        // 권한 없는 곳으로 요청
        mockMvc.perform(get("/test/access-test")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("로그인필요함")
    void 로그인필요() throws Exception {

        // 로그인 필요한 곳으로 요청
        mockMvc.perform(get("/test/access-test")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
