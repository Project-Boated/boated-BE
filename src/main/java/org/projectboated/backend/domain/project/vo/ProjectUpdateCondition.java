package org.projectboated.backend.domain.project.vo;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateCondition {

    private String name;
    private String description;
    private LocalDateTime deadline;
}
