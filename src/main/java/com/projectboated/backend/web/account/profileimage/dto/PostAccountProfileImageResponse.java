package com.projectboated.backend.web.account.profileimage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostAccountProfileImageResponse {

    private String url;

    @Builder
    public PostAccountProfileImageResponse(String url) {
        this.url = url;
    }
}
