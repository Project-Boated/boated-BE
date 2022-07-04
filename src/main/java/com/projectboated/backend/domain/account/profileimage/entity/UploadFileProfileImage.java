package com.projectboated.backend.domain.account.profileimage.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("upload_file")
public class UploadFileProfileImage extends ProfileImage {

    @OneToOne(fetch = FetchType.LAZY)
    private UploadFile uploadFile;

    @Builder
    public UploadFileProfileImage(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }
}
