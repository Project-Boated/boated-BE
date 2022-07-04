package com.projectboated.backend.domain.project.service.condition;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateCond {

    private String name;
    private String description;
    private LocalDateTime deadline;
}
