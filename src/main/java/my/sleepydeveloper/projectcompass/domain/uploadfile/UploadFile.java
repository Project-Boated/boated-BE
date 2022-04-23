package my.sleepydeveloper.projectcompass.domain.uploadfile;

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

    private String url;

    public UploadFile(String originalFileName, String saveFileName, String url) {
        this.originalFileName = originalFileName;
        this.saveFileName = saveFileName;
        this.url = url;
    }
}
