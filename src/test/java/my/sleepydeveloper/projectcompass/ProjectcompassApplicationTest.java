package my.sleepydeveloper.projectcompass;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class ProjectcompassApplicationTest {

    static MockedStatic<SpringApplication> application;

    @BeforeAll
    public static void beforeAll() {
        application = mockStatic(SpringApplication.class);
    }

    @AfterAll
    public static void afterAll() {
        application.close();
    }

    @Test
    @DisplayName("Spring Application이 제대로 돌아가는지 테스트")
    void springApplicationTest() throws Exception {
        // Given
        Mockito.when(SpringApplication.run(Mockito.any(Class.class), Mockito.any())).thenReturn(null);

        // When
        // Then
        ProjectcompassApplication.main(new String[]{"test"});
    }
}