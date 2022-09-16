package com.projectboated.backend.domain.uploadfile.entity;

import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.uploadfile.entity.exception.UploadFileNotFoundExt;
import lombok.*;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_file_id")
    private Long id;

    private String originalFileName;

    private String saveFileName;

    private String ext;

    private String mediaType;

    private Long fileSize;

    @Builder
    public UploadFile(Long id, String originalFileName, String saveFileName, String mediaType, Long fileSize) {
        this.id = id;
        this.originalFileName = removeExt(originalFileName);
        this.ext = extractExt(originalFileName);
        this.saveFileName = saveFileName;
        this.mediaType = mediaType;
        this.fileSize = fileSize;
    }

    private String extractExt(String originalFileName) {
        int lastIndexOfDot = originalFileName.lastIndexOf('.');
        return originalFileName.substring(lastIndexOfDot+1);
    }

    private String removeExt(String originalFileName) {
        int lastIndexOfDot = originalFileName.lastIndexOf('.');
        if(lastIndexOfDot == -1) {
            throw new UploadFileNotFoundExt(ErrorCode.UPLOAD_FILE_NOT_FOUND_EXT);
        }
        return originalFileName.substring(0, lastIndexOfDot);
    }

    public String getFullOriginalFileName() {
        return this.getOriginalFileName() + "." + this.getExt();
    }


}
