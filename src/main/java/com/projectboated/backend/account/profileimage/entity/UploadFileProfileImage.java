package com.projectboated.backend.account.profileimage.entity;

import com.projectboated.backend.account.profileimage.entity.exception.ProfileImageNeedsHostUrlException;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.*;

@Entity
@Getter
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
    public String getUrl(String hostUrl) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.scheme("http");
        uriBuilder.host(hostUrl);
        uriBuilder.path("/api/account/profile/profile-image");
        uriBuilder.queryParam("hash", uploadFile.getSaveFileName());
        return uriBuilder.build().toString();
    }
}
