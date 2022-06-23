package org.projectboated.backend.aws;

import org.projectboated.backend.common.basetest.BaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootTest
@Disabled
class AwsS3ServiceTest extends BaseTest {

    @Autowired
    private AwsS3Service awsS3Service;

    @Test
    @Disabled
    void uploadFile_파일업로드_성공() throws IOException {
        ClassPathResource resource = new ClassPathResource("application.yml");
        awsS3Service.uploadFile("path2/file2", resource.getFile());
    }
}