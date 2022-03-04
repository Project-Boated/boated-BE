package my.sleepydeveloper.projectcompass.domain.project.vo;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateCondition {

    private String name;
    private String description;
}
