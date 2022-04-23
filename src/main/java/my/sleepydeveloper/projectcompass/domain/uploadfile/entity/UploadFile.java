package my.sleepydeveloper.projectcompass.domain.uploadfile.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter @Getter
public class UploadFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    private String saveFileName;

    private String ext;

    private String url;

    private String mediaType;

    public UploadFile(String originalFileName, String saveFileName, String mediaType) {
        this.originalFileName = removeExt(originalFileName);
        this.ext = extractExt(originalFileName);
        this.saveFileName = saveFileName;
        this.mediaType = mediaType;
    }

    public UploadFile(String originalFileName, String saveFileName, String mediaType, String url) {
        this(originalFileName, saveFileName, mediaType);
        this.url = url;
    }

    public UploadFile(String url) {
        this.url = url;
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
