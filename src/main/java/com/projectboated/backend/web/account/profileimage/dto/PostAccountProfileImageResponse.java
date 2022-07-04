package com.projectboated.backend.web.account.profileimage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostAccountProfileImageResponse {

    private String url;

    public PostAccountProfileImageResponse(String url) {
        this.url = url;
    }
}
