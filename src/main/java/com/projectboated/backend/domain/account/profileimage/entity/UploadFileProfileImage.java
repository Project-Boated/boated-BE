package com.projectboated.backend.domain.account.profileimage.entity;

import com.projectboated.backend.domain.account.profileimage.entity.exception.ProfileImageNeedsHostUrlException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.*;
import java.util.UUID;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("upload_file")
public class UploadFileProfileImage extends ProfileImage {

    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;

    @Builder
    public UploadFileProfileImage(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    @Override
    public String getUrl(String hostUrl, Boolean isProxy) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.scheme("http");

        if(isProxy) {
            uriBuilder.host("localhost:3000");
        } else {
            if (hostUrl == null) {
                throw new ProfileImageNeedsHostUrlException(ErrorCode.PROFILE_IMAGE_NEEDS_HOST_URL);
            }
            uriBuilder.host(hostUrl);
        }
        uriBuilder.path("/api/account/profile/profile-image");

        uriBuilder.queryParam("hash", uploadFile.getSaveFileName());
        return uriBuilder.build().toString();
    }
}
