package com.projectboated.backend.common.utils;

import com.projectboated.backend.account.profileimage.entity.exception.ProfileImageNeedsHostUrlException;
import com.projectboated.backend.common.utils.exception.MultiPartFileIsEmptyException;
import com.projectboated.backend.common.utils.exception.NotImageFileException;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Component
public class HttpUtils {

    public void validateIsImageFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new MultiPartFileIsEmptyException();
        }

        String contentType = multipartFile.getContentType();
        if (contentType == null) {
            throw new NotImageFileException();
        }

        MimeType mimeType = MimeType.valueOf(contentType);
        if (!mimeType.getType().equals("image")) {
            throw new NotImageFileException();
        }
    }

    public String getHostUrl(HttpServletRequest httpServletRequest, Boolean isProxy) {
        if (isProxy) {
            return "localhost:3000";
        }

        String hostUrl = httpServletRequest.getHeader("HOST");
        if (hostUrl == null) {
            throw new ProfileImageNeedsHostUrlException();
        }
        return hostUrl;
    }

}
