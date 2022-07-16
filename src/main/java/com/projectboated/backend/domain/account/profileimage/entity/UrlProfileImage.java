package com.projectboated.backend.domain.account.profileimage.entity;

import com.projectboated.backend.domain.account.profileimage.entity.exception.ProfileImageNeedsHostUrlException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.UUID;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("url")
public class UrlProfileImage extends ProfileImage {

    private String url;

    @Builder
    public UrlProfileImage(String url) {
        this.url = url;
    }

    @Override
    public String getUrl(String hostUrl, Boolean isProxy) {
        assert url != null;
        return this.url + "?dummy=" + UUID.randomUUID();
    }
}
