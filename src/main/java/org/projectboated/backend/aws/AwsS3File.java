package org.projectboated.backend.aws;

import lombok.*;

@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
public class AwsS3File {

    private String fileName;
    private String mediaType;
    private byte[] bytes;
}
