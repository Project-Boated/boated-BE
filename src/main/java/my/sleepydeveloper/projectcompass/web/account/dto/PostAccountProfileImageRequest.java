package my.sleepydeveloper.projectcompass.web.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class PostAccountProfileImageRequest {

    @NotNull
    MultipartFile file;

    boolean isProxy;
}
