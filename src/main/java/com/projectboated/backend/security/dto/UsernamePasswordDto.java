package com.projectboated.backend.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
