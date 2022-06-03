package my.sleepydeveloper.projectcompass.domain.profileimage.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("upload_file")
public class UploadFileProfileImage extends ProfileImage {

    @OneToOne(fetch = FetchType.LAZY)
    private UploadFile uploadFile;

    public UploadFileProfileImage(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }
}
