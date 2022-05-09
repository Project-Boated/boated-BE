package my.sleepydeveloper.projectcompass.common.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicProjectData {

    public final static Long PROJECT_ID = 120L;
    public final static String PROJECT_NAME = "name";
    public final static String PROJECT_DESCRIPTION = "description";
    public final static LocalDateTime PROJECT_DEADLINE = LocalDateTime.now();
}
