package com.projectboated.backend.common.data;

import com.projectboated.backend.domain.uploadfile.entity.UploadFile;

import java.util.UUID;

public abstract class BasicDataUploadFile {

    public final static Long UPLOAD_FILE_ID = 202L;
    public final static String EXT = "ext";
    public final static String ORIGINAL_FILE_NAME = "originalFileName" + "." + EXT;
    public final static String ORIGINAL_FILE_NAME_NO_EXT = "originalFileName";
    public final static String SAVE_FILE_NAME = UUID.randomUUID().toString();
    public final static String MEDIATYPE = "mediaType";
    public static final Long FILE_SIZE = 12954L;
    public final static UploadFile UPLOADFILE = new UploadFile(UPLOAD_FILE_ID, ORIGINAL_FILE_NAME, SAVE_FILE_NAME, MEDIATYPE, FILE_SIZE);

}
