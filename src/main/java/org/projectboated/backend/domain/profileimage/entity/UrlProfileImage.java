package org.projectboated.backend.domain.profileimage.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("url")
public class UrlProfileImage extends ProfileImage {

    private String url;

    public UrlProfileImage(String url) {
        this.url = url;
    }

    public void updateUrl(String url) {
        if (this.url != null) {
            this.url = url;
        }
    }
}
