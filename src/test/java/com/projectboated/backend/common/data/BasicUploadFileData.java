package com.projectboated.backend.common.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicUploadFileData {

    public final static String ORIGINAL_FILE_NAME = "originalFile.file";
    public final static String SAVE_FILE_NAME = "saveFile";
    public final static String MEDIATYPE = "mediaType";
    public final static UploadFile UPLOADFILE_INSTANCE = new UploadFile(ORIGINAL_FILE_NAME, SAVE_FILE_NAME, MEDIATYPE);

}
