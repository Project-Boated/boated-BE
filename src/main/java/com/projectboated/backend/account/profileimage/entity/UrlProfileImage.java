package com.projectboated.backend.account.profileimage.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("url")
public class UrlProfileImage extends ProfileImage {

    private String url;

    @Builder
    public UrlProfileImage(String url) {
        this.url = url;
    }

    @Override
    public String getUrl(String hostUrl) {
        return this.url;
    }
}
