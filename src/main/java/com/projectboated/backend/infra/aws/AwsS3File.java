package com.projectboated.backend.infra.aws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AwsS3File {

    private String fileName;
    private String mediaType;
    private byte[] bytes;
}
