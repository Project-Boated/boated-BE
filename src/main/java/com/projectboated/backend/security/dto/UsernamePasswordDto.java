package com.projectboated.backend.security.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
