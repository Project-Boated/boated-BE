package com.example.projectcompass.web.account.dto;

import com.example.projectcompass.security.dto.UsernamePasswordDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto extends UsernamePasswordDto {

    @NotEmpty
    String nickname;
}
