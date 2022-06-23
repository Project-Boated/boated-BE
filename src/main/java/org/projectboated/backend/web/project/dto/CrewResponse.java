package org.projectboated.backend.web.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrewResponse {

    private Long id;
    private String username;
    private String nickname;
    private String profileImageUrl;
}
