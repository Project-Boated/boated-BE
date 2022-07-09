package com.projectboated.backend.domain.uploadfile.entity;

import lombok.*;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_file_id")
    private Long id;

    private String originalFileName;

    private String saveFileName;

    private String ext;

    private String mediaType;

    @Builder
    public UploadFile(String originalFileName, String saveFileName, String mediaType) {
        this.originalFileName = removeExt(originalFileName);
        this.ext = extractExt(originalFileName);
        this.saveFileName = saveFileName;
        this.mediaType = mediaType;
    }

    private String extractExt(String originalFileName) {
        int lastIndexOfDot = originalFileName.lastIndexOf('.');
        return originalFileName.substring(lastIndexOfDot+1);
    }

    private String removeExt(String originalFileName) {
        int lastIndexOfDot = originalFileName.lastIndexOf('.');
        return originalFileName.substring(0, lastIndexOfDot);
    }
}
