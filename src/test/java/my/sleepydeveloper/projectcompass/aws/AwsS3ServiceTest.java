package my.sleepydeveloper.projectcompass.aws;

import my.sleepydeveloper.projectcompass.common.basetest.BaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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