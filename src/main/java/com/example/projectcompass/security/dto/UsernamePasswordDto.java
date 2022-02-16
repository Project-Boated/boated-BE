package com.example.projectcompass.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
