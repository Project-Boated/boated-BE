package my.sleepydeveloper.projectcompass.aws;

import lombok.*;

@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
public class AwsS3File {

    private byte[] bytes;
    private String mediaType;
    private String fileName;
}
